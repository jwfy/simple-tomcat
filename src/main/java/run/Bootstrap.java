package run;

import tomcat.Pipeline;
import tomcat.core.StandardContext;
import tomcat.core.StandardWrapper;
import tomcat.http.HttpConnector;
import tomcat.valve.HeadValve;
import tomcat.valve.TestValve;

/**
 * Created by junhong on 17/9/7.
 */
public class Bootstrap {

    public static void main(String[] args){

        StandardContext standardContext = new StandardContext();
        standardContext.setName("standarContext");

        standardContext.addServletMapping("/primitive", "primitive");
        standardContext.addServletMapping("/basic", "basic");

        StandardWrapper w1 = new StandardWrapper();
        w1.setName("primitive");
        w1.setServletClass("PrimitiveServlet");

        StandardWrapper w2 = new StandardWrapper();
        w2.setName("basic");
        w2.setServletClass("BasicServlet");
        // 以上两个wrap已经构造好了

        try {
            standardContext.addChild(w1);
            standardContext.addChild(w2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pipeline pipeline = standardContext.getPipeline();

        HeadValve headValve = new HeadValve();
        headValve.setContainer(standardContext);
        TestValve testValve = new TestValve();
        testValve.setContainer(standardContext);

        pipeline.addValve(headValve);
        pipeline.addValve(testValve);

        HttpConnector httpConnector = new HttpConnector(8081);
        httpConnector.setContainer(standardContext);
        httpConnector.start();
    }

}
