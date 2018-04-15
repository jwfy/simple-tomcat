package tomcat.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomcat.Container;
import tomcat.Context;
import tomcat.Pipeline;
import tomcat.Service;
import tomcat.Wrapper;
import tomcat.common.WebXmlParse;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

/**
 * Created by junhong on 17/9/16.
 */
public class StandardContext extends LifecycleBase implements Context {

    private Logger logger = LoggerFactory.getLogger(StandardContext.class);

    private String name;

    private Pipeline pipeline = new StandardPipeline(this);;

    private Container parent;

    private Map<String, Container> child = null; // 儿子是儿子,和可以映射servlet的wrapper还是有区别的

    private Map<String, String> servletMap = null;   // 把匹配的URL规则映射到具体的wrapper名字上

    private ApplicationContext context = null;

    private Service service = null;
    // 在Tomcat中这个是隶属于engine中的,但是该项目是simple,只有两层容器结构
    // 但是在这里是直接赋值好的

    public StandardContext(Service service) {
        this.service = service;
        this.pipeline.setBasicValve(new StandardContextValve());
        this.child = new HashMap<>();
        this.servletMap = new HashMap<>();

        // 解析web.xml 文件获取其中的配置属性
        WebXmlParse.parse(this);
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    @Override
    public void setParent(Container container) {
        this.parent = container;
    }

    @Override
    public void addWrapper(Wrapper wrapper) {

    }

    @Override
    public Wrapper createWrapper() {
        return null;
    }

    @Override
    public Container getParent() throws Exception {
        return parent;
    }

    @Override
    public void addChild(Container container) throws Exception {
        container.setParent(this);
        child.put(container.getName(), container);
    }

    public void addServletMapping(String pattern, String name){
        servletMap.put(pattern, name);
    }

    @Override
    public Container findChild(String name) {

        for(Map.Entry<String, Container> entry: child.entrySet()){
            if(entry.getKey().equalsIgnoreCase(name))
                return entry.getValue();
        }
        return null;
    }

    @Override
    public Container[] findChildren() {
        return child.values().toArray(new Container[1]);
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
    public Wrapper findWrapper(String uri) {
        // 第一步先通过uri获取到对应的servletName
        String wrapName = null;
        for(Map.Entry<String, String> entry : this.servletMap.entrySet()){
            String pattern = entry.getKey();
            if(uri.contains(pattern)){
                wrapName = entry.getValue();
                break;
            }
        }

        if(wrapName == null || wrapName.length() <=0) return null;
        return (Wrapper) child.getOrDefault(wrapName, null);
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }

    @Override
    public ServletContext getServletContext() {
        if (context == null) {
            context = new ApplicationContext(this);
        }
        return context.getFacade();
    }

    public void start() throws Exception {
        fireLifecycleEvent(BEFORE_START_EVENT, null);

        fireLifecycleEvent(START_EVENT, null);

        for (Container child : findChildren()) {
            if (!child.getState().isAvailable()) {
                child.start();
            }
        }

        fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    public void stop() throws Exception {

        logger.info("stop");

        fireLifecycleEvent(BEFORE_STOP_EVENT, null);

        fireLifecycleEvent(STOP_EVENT, null);

        fireLifecycleEvent(AFTER_STOP_EVENT, null);
    }
}
