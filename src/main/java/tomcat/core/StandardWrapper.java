package tomcat.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.*;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;
import servlet.PrimitiveServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by junhong on 17/9/15.
 */
public class StandardWrapper extends LifecycleBase implements ServletConfig, Wrapper {

    private static final Logger logger = LoggerFactory.getLogger(StandardWrapper.class);

    private String name;
    private Servlet instance;
    private String servletClass;
    private Pipeline pipeline = new StandardPipeline(this);;
    private Container parent;
    private boolean singleThreadModel = false;
    private final StandardWrapperFacade facade = new StandardWrapperFacade(this);
    protected HashMap<String, String> parameters = new HashMap<>();

    // TODO: 17/9/15 这里没有设置这个servlet的时候啊? 应该是需要默认启动记载的吧

    public StandardWrapper() {
        pipeline.setBasicValve(new StandardWrapperValve());
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override
    public Servlet alloate() {
        this.instance = loadServlet();
        return this.instance;
    }

    public Servlet loadServlet(){
        if(!singleThreadModel && instance != null)
            return instance;

        PrintStream out = System.out;

        Servlet servlet = null;

        long time1 = System.currentTimeMillis();
        try {
            if (servletClass == null) {
                throw new Exception("don't hava servletClass");
            }

            if(classLoader == null){
                throw new Exception("don't init classload");
            }

            // "servlet.PrimitiveServlet"
            Class clazz = classLoader.loadClass(servletClass);
            Object obj = clazz.newInstance();
            servlet = (Servlet) obj;

            servlet.init(facade);
            // 这一步是把容器和servlet挂靠上

            logger.info("load service with classload:{}, service:{}, costtime:{}", clazz.getClassLoader().toString(),
                    servlet.toString(), System.currentTimeMillis() - time1);
        } catch (Exception e){
            logger.error("loadServlet error with servletClass:{}, error:{}", servletClass, e);
        }
        return servlet;
    }

    @Override
    public void setServletClass(String name) {
        this.servletClass = name;
    }

    @Override
    public String getServletClass() {
        return this.servletClass;
    }

    public void setServletName(String name){
        setName(name);
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
        return instance;
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }

    @Override
    public void start() throws Exception {
        fireLifecycleEvent(BEFORE_START_EVENT, null);

        fireLifecycleEvent(START_EVENT, null);

        fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    @Override
    public void stop() throws Exception {

        fireLifecycleEvent(BEFORE_STOP_EVENT, null);

        fireLifecycleEvent(STOP_EVENT, null);

        fireLifecycleEvent(AFTER_STOP_EVENT, null);
    }

    @Override
    public String getServletName() {
        return getName();
    }

    @Override
    public ServletContext getServletContext() {
        // 这一步就把servlet和context给绑定了,上面的init是和wrap绑定
        // 这样一来就可以很方便的在单独的servlet实现类中获取各种所需的上下文了
        if(parent == null)
            return null;
        else if(!(parent instanceof Context))
            return null;
        else
            return ((Context) parent).getServletContext();
    }

    @Override
    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }

    @Override
    public String getInitParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }
}
