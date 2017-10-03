package tomcat;

import tomcat.core.LifecycleBase;
import tomcat.http.HttpProcessor;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

/**
 * Created by junhong on 17/9/10.
 */
public interface Connector extends Lifecycle {

    void setContainer(Container container);

    Container getContainer();

    HttpRequest createRequest();

    HttpResponse createResponse();

    void cycleProcessor(HttpProcessor processor);
}
