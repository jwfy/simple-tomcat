package tomcat;

import tomcat.core.StandardContext;
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

        standardContext = new StandardContext();
        standardContext.setName("standarContext");

        standardContext.addServletMapping("/primitive", "primitive");
        standardContext.addServletMapping("/basic", "basic");

        StandardWrapper w1 = new StandardWrapper();
        w1.setName("primitive");
        w1.setServletClass("PrimitiveServlet");
        w1.addLifecycleListener(listener);

        StandardWrapper w2 = new StandardWrapper();
        w2.setName("basic");
        w2.setServletClass("BasicServlet");
        w2.addLifecycleListener(listener);

        // 以上两个wrap已经构造好了

        try {
            standardContext.addChild(w1);
            standardContext.addChild(w2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        standardContext.addLifecycleListener(listener);

        Pipeline pipeline = standardContext.getPipeline();

        pipeline.addValve(new HeadValve());
        pipeline.addValve(new TestValve());

        HttpConnector httpConnector = new HttpConnector(8081);
        httpConnector.setContainer(standardContext);
        httpConnector.start();

        try {
            standardContext.start();
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
