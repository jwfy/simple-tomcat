package tomcat.core;


import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;
import tomcat.http.RequestFacade;
import tomcat.http.ResponseFacade;
import tomcat.servlet.PrimitiveServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by junhong on 17/9/11.
 */
public class PromitiveWrapper extends AbstractWrapper {

    private String name = "promitiveContainer";
    private Map<String, Container> childMap = new HashMap<>();
    private Container parent;

    @Override
    public void invoke(HttpRequest request, HttpResponse response) {
        try {
            Servlet servlet = new PrimitiveServlet();

            RequestFacade requestFacade = new RequestFacade(request);
            ResponseFacade responseFacade = new ResponseFacade(response);

            servlet.service(requestFacade, responseFacade);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
