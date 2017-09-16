package tomcat.core;



import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;


/**
 * Created by junhong on 17/9/10.
 */
public class DefaultWrapper extends AbstractWrapper {

    private String name = "defaultContainer";
    private Container parent;

    @Override
    public void invoke(HttpRequest request, HttpResponse response) {
        response.returnMessage();
    }
}
