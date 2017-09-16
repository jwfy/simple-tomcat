package tomcat.core;


import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/14.
 */
public interface Pipeline {

    void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException;

    void setBasicValve(Valve valve);

    Valve getFirst();

    Valve getBasicValve();

    void addValve(Valve valve);

    Valve[] getValves();

    void removeValve(Valve valve);

    void setContainer(Container container);

    Container getContainer();
}
