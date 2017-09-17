package tomcat;


/**
 * Created by junhong on 17/9/17.
 *
 * tomcat4-7都有这个东西,便于具体类方便的获取到容器
 *
 */
public interface Contained {

    Container getContainer();

    void setContainer(Container container);

}
