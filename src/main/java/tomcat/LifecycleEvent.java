package tomcat;

import java.util.EventObject;

/**
 * Created by junhong on 17/9/17.
 *
 * 记录生命周期的数据以及对于的容器（lifecycle）
 */
public final class LifecycleEvent extends EventObject {

    private Object data = null;

    private Lifecycle lifecycle = null;

    private String type = null;

    public LifecycleEvent(Lifecycle lifecycle, String type) {
        this(lifecycle, type, null);
    }


    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.data = data;
        this.lifecycle = lifecycle;
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public String getType() {
        return type;
    }
}
