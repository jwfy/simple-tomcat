package startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.PrimitiveServlet;
import tomcat.*;
import tomcat.core.StandardContext;
import tomcat.core.StandardWrapper;
import tomcat.http.HttpConnector;
import tomcat.valve.HeadValve;
import tomcat.valve.TestValve;

import javax.servlet.Servlet;

/**
 * Created by junhong on 17/9/7.
 */
public class Bootstrap {

    private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private Catalina catalina;

    public void start(){
        catalina = new Catalina();
        catalina.start();
    }

    public void stop(){
        if(catalina != null) {
            catalina.stop();
            return;
        }
        logger.error("catalina don't start");
    }

    public static void main(String[] args){

        String command = "start";
        if(args.length >=1)
            command = args[args.length -1];

        Bootstrap bootstrap = new Bootstrap();
        if("start".equals(command))
            bootstrap.start();
        else
            bootstrap.stop();
    }

}
