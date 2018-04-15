package tomcat.http;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomcat.Connector;
import tomcat.Container;
import tomcat.core.LifecycleBase;

/**
 * Created by junhong on 17/9/7.
 *
 * Connector 作为一个连接器使用,接收到socket请求,组合包装成为request和response,将其交给容器处理就可以了
 *
 * 此外还维护了一个HttpProcessor的集合,这是1对多的关系
 */
public class HttpConnector extends LifecycleBase implements Runnable, Connector{

    private Logger logger = LoggerFactory.getLogger(HttpConnector.class);

    private int port;

    private Container container;

    private boolean isAccept = true;

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(6, 6, 0L,
            TimeUnit.SECONDS, new ArrayBlockingQueue(200));

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }

    public HttpConnector(int port) {
        this.port = port;
        try {
            selector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void run() {
        while (isAccept){
            // TODO: 17/9/10 这个isAccept能否采用java的钩子去进行优雅关闭
            try {
                int count = selector.select();
                if(count <=0) {
                    continue;
                }

                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()){
                    SelectionKey selectionKey = (SelectionKey)it.next();
                    it.remove();
                    if(selectionKey.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    if(selectionKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        createProcessor(socketChannel);
                        socketChannel.register(selector, SelectionKey.OP_WRITE);

                        selectionKey.cancel();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(serverSocketChannel != null){
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void httpConnectorThread(){
        Thread thread = new Thread(this, "HttpConnector");
        thread.start();
    }


    private void createProcessor(SocketChannel socketChannel){
        HttpProcessor httpProcessor = new HttpProcessor(this, socketChannel);
        executor.submit(httpProcessor);
    }

    @Override
    public void start(){
        // TODO: 17/9/10 书上说这时候还会有生命周期的操作和判断
        httpConnectorThread();
    }

    @Override
    public void stop() throws Exception {
        // TODO: 17/10/3 这里肯定就是切掉流量的入口
        isAccept = false;
        logger.info("stop and don't receive request");
    }
}
