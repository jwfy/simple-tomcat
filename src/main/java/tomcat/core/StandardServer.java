package tomcat.core;

import tomcat.Container;
import tomcat.Server;
import tomcat.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junhong on 17/9/30.
 */
public class StandardServer extends LifecycleBase implements Server {

    private int port = 8085; // 接受关闭Tomcat服务的端口号

    private String Shutdown = "shutdown";

    private List<Service> serviceList = new ArrayList<>();

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getShutdown() {
        return Shutdown;
    }

    public void setShutdown(String shutdown) {
        Shutdown = shutdown;
    }

    @Override
    public void addService(Service service) {
        serviceList.add(service);
    }

    @Override
    public Service findService(String name) {
        for(Service service:serviceList){
            if(service.getName().equals(name))
                return service;
        }
        return null;
    }

    @Override
    public Service[] findServices() {
        return serviceList.toArray(new Service[1]);
    }

    @Override
    public void removeService(Service service) {
        serviceList.remove(service);
    }

    @Override
    public void start() throws Exception {

        fireLifecycleEvent(BEFORE_START_EVENT, null);

        fireLifecycleEvent(START_EVENT, null);

        for(Service service:serviceList){
            service.start();
        }

        fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    @Override
    public void stop() throws Exception {

        fireLifecycleEvent(BEFORE_STOP_EVENT, null);

        fireLifecycleEvent(STOP_EVENT, null);

        for(Service service:serviceList){
            service.stop();
        }

        fireLifecycleEvent(AFTER_STOP_EVENT, null);
    }
}
