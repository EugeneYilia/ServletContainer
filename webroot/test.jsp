<%@ page contentType="html/text;charset=utf-8" %>

<html>
    <head>
        <title>Test Page</title>
        <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    </head>
    <body>
        <%! private void renderBadge(String a) {
            System.out.println("<div class=\"alert alert-primary\" role=\"alert\">\n" +
                     a +
                    "</div><br>");
        }%>

        <div class="jumbotron jumbotron-fluid">
            <div class="container">
                <h1 class="display-4"><% out.println("Hello"); %></h1>
                <p class="lead"><%= "This is a test page for our tomcat" %></p>
                <% renderBadge("Here are some tests"); %>
            </div>
        </div>
        <br>
        <%! private int times = 3; %>
        <div class="container">
            <table class="table table-hover table-dark">
                <thead>
                   <tr>
                        <th scope="col">#</th>
                        <th scope="col">First</th>
                        <th scope="col">Last</th>
                        <th scope="col">Handle</th>
                    </tr>
                </thead>
                <tbody>
                    <% for(int i=0;i<times;i++) { %>
                    <tr>
                        <th scope="row"><%= i %></th>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </body>
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.bootcss.com/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</html>