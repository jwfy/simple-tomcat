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
        Container container = (Container)event.getLifecycle();
        logger.info("SimpleListener name:{}, event :{}", container.getName(), event.getType());
    }
}
