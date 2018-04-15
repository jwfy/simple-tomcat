package tomcat.manager;

import tomcat.Container;
import tomcat.Context;
import tomcat.Wrapper;
import tomcat.http.HttpRequest;

/**
 * Created by junhong on 17/9/16.
 *
 * todo 需要做映射
 *
 */
public class Mapper {

    /**
     * 根据uri规则选择最合适的容器去执行
     *
     * TODO 这一块需要配置xml实现，或者实现一套类似的注解的方案
     * @param request
     */
    public static void mapWrapper(HttpRequest request) throws Exception {
        Container container = request.getContainer();
        if(!(container instanceof Context))
            throw new Exception("container must belong context");

        String uri = request.getUri();
        Wrapper wrapper = ((Context) container).findWrapper(uri);
        if(wrapper == null)
            throw new Exception("don't find wrapper container with " + uri);

        MappingData mappingData = request.getMappingData();
        mappingData.setContext((Context) container);
        mappingData.setWrapper(wrapper);
    }

}
