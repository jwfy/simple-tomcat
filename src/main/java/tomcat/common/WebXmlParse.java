package tomcat.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomcat.Context;

/**
 * Created by junhong on 18/1/2.
 */
public class WebXmlParse {

    private static Logger logger = LoggerFactory.getLogger(WebXmlParse.class);

    public static void parse(Context context){
        String path = System.getProperty("user.dir");
        path = path + "/src/main/java/servlet/web.xml";




        //
        logger.info("web xml with path:{}", path);
    }

}
