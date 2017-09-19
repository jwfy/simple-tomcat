package tomcat;

/**
 * Created by junhong on 17/9/17.
 */
public interface Lifecycle {

    //生命周期内的六个事件

    public static final String START_EVENT = "start";

    public static final String BEFORE_START_EVENT = "before_start";

    public static final String AFTER_START_EVENT = "after_start";

    public static final String STOP_EVENT = "stop";

    public static final String BEFORE_STOP_EVENT = "before_stop";

    public static final String AFTER_STOP_EVENT = "after_stop";

    //观察者的管理与通知方法

    public void addLifecycleListener(LifecycleListener listener);

    public void removeLifecycleListener(LifecycleListener listener);

    public LifecycleListener[] findLifecycleListeners();

    //主题的启动与停止方法

    public void start()throws Exception;

    public void stop()throws Exception;

}
