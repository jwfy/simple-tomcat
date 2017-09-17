package tomcat;

import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/14.
 */
public interface Valve {

    Valve getNext();

    void setNext(Valve valve);

    void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException;
}
