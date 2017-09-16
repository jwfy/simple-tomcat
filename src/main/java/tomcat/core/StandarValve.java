package tomcat.core;

import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/15.
 */
public class StandarValve implements Valve {

    private Valve next;

    @Override
    public Valve getNext() {
        return next;
    }

    @Override
    public void setNext(Valve valve) {
        this.next = next;
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        request.setAttribute("test-valve", "time-----test");
    }
}
