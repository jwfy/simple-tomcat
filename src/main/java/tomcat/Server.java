package tomcat;

/**
 * Created by junhong on 17/9/30.
 */
public interface Server extends Lifecycle {

    int getPort();

    void setPort(int port);

    void addService(Service service);

    Service findService(String name);

    Service[] findServices();

    void removeService(Service service);

}
