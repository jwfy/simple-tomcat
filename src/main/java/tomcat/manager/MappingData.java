package tomcat.manager;

import tomcat.Context;
import tomcat.Wrapper;

/**
 * Created by junhong on 17/9/16.
 *
 * TODO 根据该类的数据确定需要获取的容器,和一个request绑定的
 *
 */
public class MappingData {

    // private Host host;
    // TODO: 17/9/16 不支持Host,Engine 容器

    private Context context;
    private Wrapper wrapper;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Wrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
    }
}
