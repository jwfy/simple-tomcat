package tomcat;


import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/10.
 */
public interface Container extends Lifecycle {

    void setParent(Container container);

    Container getParent() throws Exception;

    Pipeline getPipeline();

    void addChild(Container container) throws Exception;

    Container findChild(String name);

    Container[] findChildren();

    void setName(String name);

    String getName();

    void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException;
}
