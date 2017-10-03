package tomcat.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.*;
import tomcat.manager.Mapper;
import tomcat.valve.HeadValve;
import tomcat.valve.TestValve;

import javax.servlet.ServletConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junhong on 17/9/30.
 */
public class StandardService extends LifecycleBase implements Service {

    private static final Logger logger = LoggerFactory.getLogger(StandardService.class);

    private String name;

    // service 挂靠在server下面
    private Server server;

    // 该service持有的连接器
    private List<Connector> connectorList = null;

    private StandardContext context = null;

    protected final Mapper mapper = new Mapper();

    public StandardService(Server server) {
        this.server = server;
        this.context = new StandardContext();
        this.connectorList = new ArrayList<>();

        initContext();
    }

    private void initContext(){

        this.context.setName("standarContext");

        this.context.addServletMapping("/primitive", "primitive");
        this.context.addServletMapping("/basic", "basic");

        ClassLoader classLoader = new DefaultClassLoader();

        StandardWrapper w1 = new StandardWrapper();
        w1.setName("primitive");
        w1.setServletClass("servlet.PrimitiveServlet");
        w1.setClassLoader(classLoader);
        // w1.addLifecycleListener(listener);

        StandardWrapper w2 = new StandardWrapper();
        w2.setName("basic");
        w2.setServletClass("servlet.BasicServlet");
        // 如果这里不设置其classloader,则会提示未找到对应的classload,直接处理失败
        w2.setClassLoader(classLoader);
        // w2.addLifecycleListener(listener);

        try {
            this.context.addChild(w1);
            this.context.addChild(w2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // this.context.addLifecycleListener(listener);

        Pipeline pipeline = this.context.getPipeline();

        pipeline.addValve(new HeadValve());
        pipeline.addValve(new TestValve());
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(StandardContext context) {
        this.context = context;
    }

    @Override
    public void addConnector(Connector connector) {
        connectorList.add(connector);
        if(getState().isAvailable()){
            try {
                connector.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 如果可用,就需要启动该连接器,接收数据了
        }
    }

    @Override
    public Connector[] findConnectors() {
        return connectorList.toArray(new Connector[1]);
    }

    @Override
    public void removeConnector(Connector connector) {
        connectorList.remove(connector);
    }

    @Override
    public Mapper getMapper() {
        return this.mapper;
    }

    @Override
    public void start() throws Exception {
        fireLifecycleEvent(BEFORE_START_EVENT, null);

        fireLifecycleEvent(START_EVENT, null);

        context.start();

        for(Connector connector:connectorList){
            connector.start();
        }

        fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    @Override
    public void stop() throws Exception {

        fireLifecycleEvent(BEFORE_STOP_EVENT, null);

        fireLifecycleEvent(STOP_EVENT, null);

        context.stop();

        for(Connector connector:connectorList){
            connector.stop();
        }

        fireLifecycleEvent(AFTER_STOP_EVENT, null);
    }
}
