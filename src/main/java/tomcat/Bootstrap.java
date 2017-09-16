package tomcat;

import tomcat.http.HttpConnector;
import tomcat.http.HttpRequest;

/**
 * Created by junhong on 17/9/7.
 */
public class Bootstrap {

    public static void main(String[] args){
        HttpConnector httpConnector = new HttpConnector(8081);
        httpConnector.start();
    }

}
