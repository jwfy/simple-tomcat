package tomcat.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.Container;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/16.
 */
public class StandardContextValve extends ValveBase {

    private Logger logger = LoggerFactory.getLogger(StandardContextValve.class);

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        logger.info("StandardContextValve loading....");

        request.getMappingData().getWrapper().invoke(request, response);
    }
}
