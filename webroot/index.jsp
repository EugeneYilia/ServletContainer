<%@ page contentType="html/text;charset=utf-8" %>
<h1>My id is: <% out.println("Hello"); %></h1>
<%! private int id; %>
<% for (int i = 0; i < 3; i++) { %>
    <div>Loop Test</div>
<% } %>
Hello
<%! private void test(int a) {
    System.out.println(a);
}%>
