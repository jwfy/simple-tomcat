package tomcat;

import tomcat.core.StandardContext;
import tomcat.core.StandardServer;
import tomcat.core.StandardService;
import tomcat.core.StandardWrapper;
import tomcat.http.HttpConnector;
import tomcat.valve.HeadValve;
import tomcat.valve.TestValve;

/**
 * Created by junhong on 17/9/22.
 */
public class Catalina {

    private StandardContext standardContext;

    private Thread shutdownHook = null;

    public void start(){

        LifecycleListener listener = new SimpleListener();

        Server server = new StandardServer();

        Service service = new StandardService(server);

        HttpConnector httpConnector = new HttpConnector(8081);
        httpConnector.setContainer(service.getContext());
        // TODO: 17/10/3 从这个service.getContext中强制初始化了包含的容器是context,而不是engine
        service.addConnector(httpConnector);

        // TODO: 17/10/3 如果需要添加不同的connection,则直接添加即可

        server.addService(service);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(shutdownHook == null)
            shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public void stop(){
        if(shutdownHook != null)
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
        try {
            standardContext.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected class ShutdownHook extends Thread{

        @Override
        public void run() {
            try {
                Catalina.this.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
