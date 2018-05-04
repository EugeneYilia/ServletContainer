import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;


public class index_jsp extends HttpServlet {
    private int id;

    private void test(int a) {
        System.out.println(a);
    }

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        out.println("<h1>My id is:");
        out.println("Hello");
        out.println("</h1>");

        for (int i = 0; i < 3; i++) {
            out.println("<div>Loop Test</div>");
        }

        out.println("Hello");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        out.println("<h1>My id is:");
        out.println("Hello");
        out.println("</h1>");

        for (int i = 0; i < 3; i++) {
            out.println("<div>Loop Test</div>");
        }

        out.println("Hello");
    }

    public void destroy() {
    }
}
