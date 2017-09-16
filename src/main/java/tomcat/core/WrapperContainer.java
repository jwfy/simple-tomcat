package tomcat.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by junhong on 17/9/11.
 *
 * 主要是做uri规则映射的,哪个URL请求由哪个容器执行
 *
 */
public class WrapperContainer {

    private static Map<String, String> uriMap = new HashMap<>();

    private static String defaultUriMapperKey = "*";

    static {
        uriMap.put("promitive", "tomcat.core.PromitiveWrapper");
        uriMap.put(defaultUriMapperKey, "tomcat.core.DefaultWrapper");
    }

    public WrapperContainer() {

    }

    public static Container getContainerByUri(String uri){

        String loadClassName = null;


        if(uri == null || uri.length() <=0){
            loadClassName = uriMap.get(defaultUriMapperKey);
        }else{
            Iterator it = uriMap.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
                String key = entry.getKey();
                if(uri.contains(key)){
                    loadClassName = entry.getValue();
                    break;
                }
            }
        }

        if(loadClassName == null)
            loadClassName = uriMap.get(defaultUriMapperKey);
        try {
            Container container = (Container) Class.forName(loadClassName).newInstance();
            if(container != null) return container;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
