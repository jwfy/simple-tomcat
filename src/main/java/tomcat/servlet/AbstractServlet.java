package tomcat.servlet;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by junhong on 17/9/4.
 */
public abstract class AbstractServlet implements Servlet {

    protected String successHead = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: %d\r\n" +
            "Server-name: junhong\r\n" +
            "\r\n" +
            "%s";

    protected String failedHead = "HTTP/1.1 500 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: %d\r\n" +
            "Server-name: junhong\r\n" +
            "\r\n" +
            "%s";

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String complete = null;
        try{
            String message = getMessage(req, res);
             complete = String.format(successHead, message.length(), message);
        } catch (Exception e){
            String message = "<H1>500 ERROR<H1>";
             complete = String.format(failedHead, message.length(), message);
        }
        PrintWriter pw = res.getWriter();
        pw.print(complete);
        pw.close();
    }

    protected abstract String getMessage(ServletRequest req, ServletResponse res) throws ServletException, IOException;
}
