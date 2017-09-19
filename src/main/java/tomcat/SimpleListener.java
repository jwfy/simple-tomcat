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
        Lifecycle lifecycle = event.getLifecycle();
        logger.info("SimpleListener event with :{}", event.getType());
    }
}
