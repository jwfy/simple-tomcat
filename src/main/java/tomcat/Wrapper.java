package tomcat;

import tomcat.Container;

import javax.servlet.Servlet;

/**
 * Created by junhong on 17/9/15.
 *
 * 最低级的一种容器
 */
public interface Wrapper extends Container {

    Servlet alloate();
    // 获得一个可用的servlet,进行运行处理

    // 设置该wrap包含的servlet
    void setServletClass(String name);

    String getServletClass();

}
