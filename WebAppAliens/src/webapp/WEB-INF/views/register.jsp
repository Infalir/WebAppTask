<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <title>Title</title></head>
<body>
<div class="container">
    <section id="content"><p><font color="red">${errorRegister}</font></p>
        <form action="${pageContext.servletContext.contextPath}/controller?command=register_new_user" method="POST"><h1>
            Регистрация нового пользователя </h1>
            <div><input placeholder="Input your username" required="" name="newLoginName" type="text"/></div>
            <div><input placeholder="Input your password" id="password" required="" name="newPassword" type="password"/>
            </div>
            <div><input type="submit" value="Sign up"/></div>
        </form>
    </section>
</div>
</body>
</html>