package tomcat.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomcat.Connector;
import tomcat.Container;

/**
 * Created by junhong on 17/9/7.
 */
public class HttpProcessor implements Runnable {

    private Logger logger = LoggerFactory.getLogger(HttpProcessor.class);

    private Connector connector;

    private Container container;

    private SocketChannel socketChannel;

    private boolean isRun = false; // 判断其是否处于运行状态

    public HttpProcessor(Connector connector, SocketChannel socketChannel) {
        this.connector = connector;
        this.container = connector.getContainer();
        this.socketChannel = socketChannel;
    }

    private void parse(){
        String successHead = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Server-name: junhong\r\n" +
                "\r\n" +
                "%s";

        Random random = new Random();
        long sleepTime = random.nextInt(500);
        logger.info("write message and costtime：{}", sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String message = String.format(successHead, "Hello World");
        try {
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void run() {
        if(socketChannel == null){
            logger.error("error socker channel");
            return;
        }

        try {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setSocketChannel(socketChannel);
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setHttpRequest(httpRequest);
            httpRequest.parse();

            parse();

            /*
            String url = httpRequest.getUri();
            if(url.endsWith("ico") || url.endsWith("gif") || url.endsWith("jpg")) {
                logger.error("processor don't deal url:{}", url);
            }else {
                Mapper.mapWrapper(httpRequest);
                container.invoke(httpRequest, httpResponse);
            }
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRun() {
        return isRun;
    }
}
