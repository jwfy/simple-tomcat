package tomcat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by junhong on 17/9/18.
 */
public class SimpleListener implements LifecycleListener {

    private static Logger logger = LoggerFactory.getLogger(SimpleListener.class);

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        String name = null;
        if(event.getLifecycle() instanceof Container)
            name = ((Container) event.getLifecycle()).getName();
        else if(event.getLifecycle() instanceof Service)
            name = ((Service) event.getLifecycle()).getName();
        else if(event.getLifecycle() instanceof Server)
            name = "Server";
        logger.info("SimpleListener name:{}, event :{}", name, event.getType());
    }
}
