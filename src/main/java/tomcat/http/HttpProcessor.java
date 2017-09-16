package tomcat.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.core.Connector;
import tomcat.core.Container;
import tomcat.core.WrapperContainer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by junhong on 17/9/7.
 */
public class HttpProcessor implements Runnable {

    private Logger logger = LoggerFactory.getLogger(HttpProcessor.class);

    private Connector connector;

    private boolean available = false;

    private Socket socket;

    private boolean isRun = false; // 判断其是否处于运行状态

    public HttpProcessor(Connector connector) {
        this.connector = connector;
    }

    private void processor(Socket socket){
        logger.info("processor deal socket with :{}", socket);

        HttpRequest httpRequest = connector.createRequest();
        HttpResponse httpResponse = connector.createResponse();
        httpResponse.setHttpRequest(httpRequest);

        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            httpRequest.setInputStream(inputStream);
            httpResponse.setOutputStream(outputStream);

            // 需要解析inputstream
            httpRequest.parse();

            String uri = httpRequest.getUri();
            Container container = WrapperContainer.getContainerByUri(uri);
            assert container != null;

            container.invoke(httpRequest, httpResponse);

        } catch (Exception e) {
            logger.error("processor error with:{}", e);
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connector.cycleProcessor(this);
        }

    }

    private synchronized Socket await(){
        while (!available){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Socket socket = this.socket;
        this.socket = null; // 当前socket设置为null
        available = false;
        notifyAll();
        return socket;
    }

    public synchronized void assign(Socket socket){

        while (available){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("assign add new socket with :{}, avaliable:{}", socket, available);

        this.socket = socket;
        available = true;
        notifyAll();
    }

    @Override
    public void run() {
        isRun = true;

        while (true){
            Socket socket = await();
            if(socket == null) continue;
            processor(socket);
        }
    }

    public boolean isRun() {
        return isRun;
    }
}
