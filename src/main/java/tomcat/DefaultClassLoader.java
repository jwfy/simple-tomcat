package tomcat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by junhong on 17/9/24.
 *
 * 自定义了一个servlet的classLoader
 * 而且只处理在文件夹servlet下面的service
 */
public class DefaultClassLoader extends ClassLoader {

    private static final String CLASS_FILE_SUFFIX = ".class";
    private String SERVLET_CLSSS_ROOT;
    private Map<String, Class> cacheMap = new HashMap<>();

    public DefaultClassLoader(String SERVLET_CLSSS_ROOT) {
        super();
        this.SERVLET_CLSSS_ROOT = SERVLET_CLSSS_ROOT;
    }

    public DefaultClassLoader(){
        super();
        this.SERVLET_CLSSS_ROOT = System.getProperty("user.dir") + "/target/classes/";
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = getFileName(name);
        File file = new File(fileName);
        Class clazz = null;

        try {
            FileInputStream is = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {

            }
            byte[] data = bos.toByteArray();
            is.close();
            bos.close();
            clazz =  defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            clazz = super.findClass(name);
        }
        return clazz;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if(!name.startsWith("servlet"))
            // 这里就是分别使用不同的类加载器,super就是调用apploadclass
            return super.loadClass(name, resolve);

        Class cacheClazz = cacheMap.getOrDefault(name, null);
        if(cacheClazz !=null)
            return cacheClazz;

        Class clazz = findClass(name);
        if(resolve){
            resolveClass(clazz);
        }
        cacheMap.put(name, clazz);
        return clazz;
    }

    private String getFileName(String name){
        name = name.replace(".", "/");

        StringBuilder sb = new StringBuilder();
        sb.append(SERVLET_CLSSS_ROOT);
        sb.append(name);
        sb.append(CLASS_FILE_SUFFIX);
        return sb.toString();
    }
}
