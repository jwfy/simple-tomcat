package tomcat.core;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * Created by junhong on 17/9/27.
 *
 * 主要是为了设配servlet,传入到servlet当做config,包装起来了,对servlet进行屏蔽操作
 *
 */
public class StandardWrapperFacade implements ServletConfig {

    private final ServletConfig config;
    private ServletContext context = null;

    public StandardWrapperFacade(StandardWrapper config) {
        super();
        this.config = config;
    }

    @Override
    public String getServletName() {
        return config.getServletName();
    }

    @Override
    public ServletContext getServletContext() {
        if(context == null)
            context = config.getServletContext();
        return context;
    }

    @Override
    public String getInitParameter(String name) {
        return config.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return config.getInitParameterNames();
    }
}
