package servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Created by junhong on 17/9/24.
 */
public class BasicServlet extends AbstractServlet {

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }

    @Override
    protected String getMessage(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        return "Welcome";
    }
}
