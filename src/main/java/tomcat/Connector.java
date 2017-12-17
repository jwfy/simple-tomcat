package tomcat;

/**
 * Created by junhong on 17/9/10.
 */
public interface Connector extends Lifecycle {

    void setContainer(Container container);

    Container getContainer();
}
