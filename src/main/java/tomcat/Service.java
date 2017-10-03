package tomcat;

import tomcat.core.StandardContext;
import tomcat.manager.Mapper;

/**
 * Created by junhong on 17/9/30.
 *
 * service 是连接四级容器和连接器的组件,同样需要在生命周期内监听着
 */
public interface Service extends Lifecycle {

    String getName();

    void setName(String name);

    Server getServer();

    void setServer(Server server);

    Context getContext();

    void setContext(StandardContext context);

    void addConnector(Connector connector);

    Connector[] findConnectors();

    void removeConnector(Connector connector);

    Mapper getMapper();

}
