package tomcat.core;

import tomcat.*;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;
import tomcat.servlet.PrimitiveServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by junhong on 17/9/15.
 */
public class StandardWrapper implements Wrapper, Lifecycle {

    private String name;
    private Servlet servlet;
    private String servletName;
    private Pipeline pipeline = new StandardPipeline(this);;
    private Container parent;
    private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);

    // TODO: 17/9/15 这里没有设置这个servlet的时候啊? 应该是需要默认启动记载的吧


    public StandardWrapper() {
        pipeline.setBasicValve(new StandardWrapperValve());
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override
    public Servlet alloate() {
        return new PrimitiveServlet();
    }

    @Override
    public void setServletClass(String name) {
        this.servletName = name;
    }

    @Override
    public Container getParent() throws Exception {
        return parent;
    }

    @Override
    public void addChild(Container container) throws Exception {
        throw new Exception("can't add child node");
    }

    @Override
    public Container findChild(String name) {
        return null;
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public Servlet getServlet() {
        return servlet;
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }


    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.addLifecycleListener(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.removeLifecycleListener(listener);
    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycleSupport.findLifecycleListeners();
    }

    @Override
    public void start() throws Exception {
        lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);

        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

        lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    @Override
    public void stop() throws Exception {

        lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);

        lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

        lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
    }
}
