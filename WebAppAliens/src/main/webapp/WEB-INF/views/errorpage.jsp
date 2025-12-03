<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java"
%>
<fmt:setBundle basename="text" var="text"/>
<html>
<head>
    <title>Show Error Page</title>
</head>
<body>

<h1>ERROR...</h1>
<table width="100%" border="1">

    <tr valign="top">
        <td><b>Status code:</b></td>
        <td>${error_message}</td>
    </tr>
</table>
</body>
</html>