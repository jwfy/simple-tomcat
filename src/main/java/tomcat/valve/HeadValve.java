package tomcat.valve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.core.BaseValve;
import tomcat.Container;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/16.
 */
public class HeadValve extends BaseValve {

    private Logger logger = LoggerFactory.getLogger(HeadValve.class);

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        logger.info("Head Valve loading...");
        getNext().invoke(request, response);
    }
}
