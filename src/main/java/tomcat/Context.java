package tomcat;

import javax.servlet.ServletContext;

/**
 * Created by junhong on 17/9/15.
 */
public interface Context extends Container{

    void addWrapper(Wrapper wrapper);

    // TODO: 17/9/16 当前是没用的吧,wrapper自然有地方可以生成了,无需在这里了
    Wrapper createWrapper();

    // TODO: 17/9/16 貌似这个也是无用的
    Wrapper findWrapper(String uri);

    ServletContext getServletContext();

    Service getService();

}
