package tomcat.core;

import tomcat.*;
import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by junhong on 17/9/16.
 */
public class StandardContext implements Context, Lifecycle {

    private String name;

    private Pipeline pipeline = new StandardPipeline(this);;

    private Container parent;

    private Map<String, Container> child = null; // 儿子是儿子,和可以映射servlet的wrapper还是有区别的

    private Map<String, String> servletMap = null;   // 把匹配的URL规则映射到具体的wrapper名字上

    private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);

    public StandardContext() {
        pipeline.setBasicValve(new StandardContextValve());
        child = new HashMap<>();
        servletMap = new HashMap<>();
    }

    public StandardContext(String name) {
        this();
        this.name = name;
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
        Iterator it = child.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Container> entry = (Map.Entry<String, Container>) it.next();
            if (entry.getKey().equals(name)) {
                return entry.getValue();
            }
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
