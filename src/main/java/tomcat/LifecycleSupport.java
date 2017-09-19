package tomcat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junhong on 17/9/17.
 *
 */
public final class LifecycleSupport {

    private Lifecycle lifecycle = null;
    private List<LifecycleListener> listenerList = new ArrayList<>();

    public LifecycleSupport(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void addLifecycleListener(LifecycleListener lifecycleListener){
        listenerList.add(lifecycleListener);
    }

    public void removeLifecycleListener(LifecycleListener lifecycleListener){
        for(LifecycleListener listener:listenerList){
            if(listener == lifecycleListener){
                listenerList.remove(listener);
                return;
            }
        }
    }

    public LifecycleListener[] findLifecycleListeners(){
        return listenerList.toArray(new LifecycleListener[1]);
    }

    /**
     * 生成一个事件,通知给所有的观察者
     * @param type
     * @param data
     */
    public void fireLifecycleEvent(String type, Object data){
        LifecycleEvent lifecycleEvent = new LifecycleEvent(data, lifecycle, type);

        for(LifecycleListener listener:listenerList){
            listener.lifecycleEvent(lifecycleEvent);
        }
    }

}
