package tomcat.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.Connector;
import tomcat.Container;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

/**
 * Created by junhong on 17/9/7.
 */
public class HttpProcessor implements Runnable {

    private Logger logger = LoggerFactory.getLogger(HttpProcessor.class);

    private Connector connector;

    private Container container;

    private boolean available = false;

    private SocketChannel socketChannel;

    private boolean isRun = false; // 判断其是否处于运行状态

    public HttpProcessor(Connector connector) {
        this.connector = connector;
        this.container = connector.getContainer();
    }

    public HttpProcessor(SocketChannel socketChannel) {
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
        parse();
    }

    public boolean isRun() {
        return isRun;
    }
}
