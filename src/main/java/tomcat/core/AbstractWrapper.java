package tomcat.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by junhong on 17/9/11.
 */
public abstract class AbstractWrapper implements Container {

    protected String name;
    protected Map<String, Container> childMap = new HashMap<>();
    protected Container parent;

    @Override
    public Container getParent() {
        return parent;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(Container container) throws Exception {
        throw new Exception("don't add child");
    }

    @Override
    public Container findChild(String name) {
        return childMap.getOrDefault(name, null);
    }

    @Override
    public Container[] findChildren() {
        return childMap.values().toArray(new Container[1]);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }



}
