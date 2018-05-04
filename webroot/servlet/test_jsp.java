import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;


public class test_jsp extends HttpServlet {
    private int times = 3;

    private void renderBadge(String a) {
        System.out.println(
            "<div class=\"alert alert-primary\" role=\"alert\">\n" + a +
            "</div><br>");
    }

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        out.println("<html>");
        out.println("\t<head>");
        out.println("\t\t<title>Test Page</title>");
        out.println(
            "\t\t<link rel=\"stylesheet\" href=\"https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">");
        out.println("\t</head>");
        out.println("\t<body>");
        out.println("\t\t");
        out.println("\t\t<div class=\"jumbotron jumbotron-fluid\">");
        out.println("\t\t\t<div class=\"container\">");
        out.println("\t\t\t\t<h1 class=\"display-4\">");
        out.println("Hello");
        out.println("</h1>");
        out.println("\t\t\t\t<p class=\"lead\">");
        out.println("This is a test page for our tomcat");
        out.println("</p>");
        out.println("\t\t\t\t");
        renderBadge("Here are some tests");
        out.println("\t\t\t</div>");
        out.println("\t\t</div>");
        out.println("\t\t<br>");
        out.println("\t\t");
        out.println("\t\t<div class=\"container\">");
        out.println("\t\t\t<table class=\"table table-hover table-dark\">");
        out.println("\t\t\t\t<thead>");
        out.println("\t\t\t\t   <tr>");
        out.println("\t\t\t\t\t\t<th scope=\"col\">#</th>");
        out.println("\t\t\t\t\t\t<th scope=\"col\">First</th>");
        out.println("\t\t\t\t\t\t<th scope=\"col\">Last</th>");
        out.println("\t\t\t\t\t\t<th scope=\"col\">Handle</th>");
        out.println("\t\t\t\t\t</tr>");
        out.println("\t\t\t\t</thead>");
        out.println("\t\t\t\t<tbody>");
        out.println("\t\t\t\t\t");

        for (int i = 0; i < times; i++) {
            out.println("\t\t\t\t\t<tr>");
            out.println("\t\t\t\t\t\t<th scope=\"row\">");
            out.println(i);
            out.println("</th>");
            out.println("\t\t\t\t\t\t<td>Mark</td>");
            out.println("\t\t\t\t\t\t<td>Otto</td>");
            out.println("\t\t\t\t\t\t<td>@mdo</td>");
            out.println("\t\t\t\t\t</tr>");
            out.println("\t\t\t\t\t");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        out.println("<html>");
        out.println("\t<head>");
        out.println("\t\t<title>Test Page</title>");
        out.println(
            "\t\t<link rel=\"stylesheet\" href=\"https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">");
        out.println("\t</head>");
        out.println("\t<body>");
        out.println("\t\t");
        out.println("\t\t<div class=\"jumbotron jumbotron-fluid\">");
        out.println("\t\t\t<div class=\"container\">");
        out.println("\t\t\t\t<h1 class=\"display-4\">");
        out.println("Hello");
        out.println("</h1>");
        out.println("\t\t\t\t<p class=\"lead\">");
        out.println("This is a test page for our tomcat");
        out.println("</p>");
        out.println("\t\t\t\t");
        renderBadge("Here are some tests");
        out.println("\t\t\t</div>");
        out.println("\t\t</div>");
        out.println("\t\t<br>");
        out.println("\t\t");
        out.println("\t\t<div class=\"container\">");
        out.println("\t\t\t<table class=\"table table-hover table-dark\">");
        out.println("\t\t\t\t<thead>");
        out.println("\t\t\t\t   <tr>");
        out.println("\t\t\t\t\t\t<th scope=\"col\">#</th>");
        out.println("\t\t\t\t\t\t<th scope=\"col\">First</th>");
        out.println("\t\t\t\t\t\t<th scope=\"col\">Last</th>");
        out.println("\t\t\t\t\t\t<th scope=\"col\">Handle</th>");
        out.println("\t\t\t\t\t</tr>");
        out.println("\t\t\t\t</thead>");
        out.println("\t\t\t\t<tbody>");
        out.println("\t\t\t\t\t");

        for (int i = 0; i < times; i++) {
            out.println("\t\t\t\t\t<tr>");
            out.println("\t\t\t\t\t\t<th scope=\"row\">");
            out.println(i);
            out.println("</th>");
            out.println("\t\t\t\t\t\t<td>Mark</td>");
            out.println("\t\t\t\t\t\t<td>Otto</td>");
            out.println("\t\t\t\t\t\t<td>@mdo</td>");
            out.println("\t\t\t\t\t</tr>");
            out.println("\t\t\t\t\t");
        }
    }

    public void destroy() {
    }
}
