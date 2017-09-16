package tomcat.core;

/**
 * Created by junhong on 17/9/15.
 */
public interface Context extends Container{

    void addWrapper(Wrapper wrapper);

    Wrapper createWrapper();
}
