package com.EugeneStudio.testConnection;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

    public void init(ServletConfig var1) {
        //System.out.println("ModernServlet -- init");
    }

    public void doGet(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
        var2.setContentType("text/html");
        PrintWriter var3 = var2.getWriter();
        var3.println("<html>");
        var3.println("<head>");
        var3.println("<title>Modern Servlet</title>");
        var3.println("</head>");
        var3.println("<body>");
        var3.println("<h2>Headers</h2");

    }
}
