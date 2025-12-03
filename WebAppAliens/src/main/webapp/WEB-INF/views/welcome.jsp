welcome.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title> Welcome</title> </head>
<body>
<nav role="navigation" class="navbar navbar-default">
    <div>
        <nav class="menu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/controller?command=login_page">Login</a></li>
                <li><a href="${pageContext.servletContext.contextPath}/controller?command=sign_out">Logout</a></li>
                <li style="float:right;">
            </ul>
        </nav>
    </div>
</nav>
<div class="container"><h4>Welcome, ${username} </h4>
</div>
</body>
</html>