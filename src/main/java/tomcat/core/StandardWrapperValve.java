package tomcat.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.Container;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;
import tomcat.http.HttpRequestFacade;
import tomcat.http.HttpResponseFacade;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/15.
 */
public class StandardWrapperValve extends BaseValve {

    private Logger logger = LoggerFactory.getLogger(StandardWrapperValve.class);

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        logger.info("test-valve ----> time-----test");

        StandardWrapper wrapper = (StandardWrapper) getContainer();
        Servlet servlet = wrapper.alloate();

        HttpRequestFacade requestFacade = new HttpRequestFacade(request);
        HttpResponseFacade responseFacade = new HttpResponseFacade(response);

        servlet.service(requestFacade, responseFacade);
    }
}
