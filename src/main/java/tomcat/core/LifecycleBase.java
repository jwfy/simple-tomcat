package tomcat.core;

import tomcat.Lifecycle;
import tomcat.LifecycleEvent;
import tomcat.LifecycleListener;
import tomcat.LifecycleState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junhong on 17/9/21.
 */
public abstract class LifecycleBase implements Lifecycle {

    protected final List<LifecycleListener> lifecycleListenerList = new ArrayList<>();

    protected LifecycleState state = LifecycleState.NEW;

    protected ClassLoader classLoader;

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleListenerList.add(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleListenerList.remove(listener);
    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycleListenerList.toArray(new LifecycleListener[0]);
    }

    protected void fireLifecycleEvent(String type, Object data) {
        LifecycleEvent event = new LifecycleEvent(this, type, data);
        for (LifecycleListener listener : lifecycleListenerList) {
            listener.lifecycleEvent(event);
        }
    }

    @Override
    public LifecycleState getState() {
        return state;
    }

    protected void setState(LifecycleState state){
        this.state = state;
    }

    @Override
    public String getStateName() {
        return state.getLifecycleEvent();
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;
}
