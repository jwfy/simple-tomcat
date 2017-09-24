package servlet;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by junhong on 17/9/3.
 */
public class PrimitiveServlet extends AbstractServlet {

    protected String successHead = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: %d\r\n" +
            "Server-name: junhong\r\n" +
            "\r\n" +
            "%s";

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }

    @Override
    protected String getMessage(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        return "<h1 style='color:red'>Hello World</h1>";
    }
}
