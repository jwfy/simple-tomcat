package tomcat.http;


import tomcat.Connector;
import tomcat.Container;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

/**
 * Created by junhong on 17/9/7.
 *
 * Connector 作为一个连接器使用,接收到socket请求,组合包装成为request和response,将其交给容器处理就可以了
 *
 * 此外还维护了一个HttpProcessor的集合,这是1对多的关系
 */
public class HttpConnector implements Runnable, Connector{

    private ServerSocket serverSocket;

    private int port;

    private int processorNum = 0;

    private Container container;

    private boolean isAccept = true;

    private Stack<HttpProcessor> processorStack = new Stack<HttpProcessor>();
    // 默认的启动2个HttpProcessor

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }

    public HttpConnector(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
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
    public HttpRequest createRequest() {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setConnector(this);
        return httpRequest;
    }

    @Override
    public HttpResponse createResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setConnector(this);
        return httpResponse;
    }

    @Override
    public void run() {
        while (isAccept){
            // TODO: 17/9/10 这个isAccept能否采用java的钩子去进行优雅关闭
            Socket socket = null;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                // TODO: 17/9/10 需要打印日志
                continue;
            }

            // 把socket交给HttpProcessor处理,他会生成对于的HttpResponse和HttpRequest
            HttpProcessor httpProcessor = createProcessor();
            httpProcessor.assign(socket);
        }
    }

    private void httpConnectorThread(){
        Thread thread = new Thread(this, "HttpConnector");
        thread.start();
    }

    private void newProcessor(){
        int i = 2;
        while (i-- > 0){
            HttpProcessor httpProcessor = new HttpProcessor(this);
            // 这时候还什么都没做,就是创建了个新的对象吧
            processorStack.push(httpProcessor);
        }
    }

    private HttpProcessor createProcessor(){
        HttpProcessor httpProcessor = null;

        if(processorStack.isEmpty())
            httpProcessor = new HttpProcessor(this);
        else
            httpProcessor = (HttpProcessor)processorStack.pop();
        boolean isRun = httpProcessor.isRun();
        if(!isRun){
            new Thread(httpProcessor, "HttpProcessor-" + processorNum).start();
            processorNum++;
            // TODO: 17/9/17 可以加上对processor处理的请求数做一个统计 
        }
        return httpProcessor;
    }

    @Override
    public void cycleProcessor(HttpProcessor processor){
        processorStack.push(processor);
        // 回收能够接受请求的processor
    }

    public void start(){
        // TODO: 17/9/10 书上说这时候还会有生命周期的操作和判断
        httpConnectorThread();
        newProcessor();
    }
}
