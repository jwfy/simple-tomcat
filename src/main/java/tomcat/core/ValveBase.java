package tomcat.core;

import tomcat.Contained;
import tomcat.Container;
import tomcat.Valve;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/16.
 */
public abstract class ValveBase implements Valve, Contained {

    protected Valve next = null;
    protected Container container = null;

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public Valve getNext() {
        return next;
    }

    @Override
    public void setNext(Valve valve) {
        this.next = valve;
    }

    /**
     * TODO 在所有自定义的valve中,都必须加上getNext().invoke
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public abstract void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException;

}
