package tomcat.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.Connector;
import tomcat.Container;
import tomcat.manager.Mapper;

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

    private Container container;

    private boolean available = false;

    private Socket socket;

    private boolean isRun = false; // 判断其是否处于运行状态

    public HttpProcessor(Connector connector) {
        this.connector = connector;
        this.container = connector.getContainer();
    }

    private void processor(Socket socket){
        // logger.info("processor deal socket with :{}", socket);

        HttpRequest httpRequest = connector.createRequest();
        HttpResponse httpResponse = connector.createResponse();
        httpResponse.setHttpRequest(httpRequest);

        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            httpRequest.setInputStream(inputStream);
            httpResponse.setOutputStream(outputStream);

            httpRequest.setContainer(container);
            httpResponse.setContainer(container);

            // 需要解析inputstream
            httpRequest.parse();

            // TODO: 17/9/16 tomcat7是根据数据配置好MappingData数据
            String url = httpRequest.getUri();
            if(url.endsWith("ico") || url.endsWith("gif") || url.endsWith("jpg")) {
                logger.error("processor don't deal url:{}, socket:{}", url, socket);
            }else {
                Mapper.mapWrapper(httpRequest);
                container.invoke(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            logger.error("processor error with:{}", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
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
        // logger.info("assign add new socket with :{}, avaliable:{}", socket, available);

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
