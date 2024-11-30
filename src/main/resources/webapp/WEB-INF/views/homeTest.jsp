<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <h2>Hello world</h2>
    <h2>This is home page from jsp</h2>
    <p><%=request.getAttribute("message")%></p>

</body>
</html>
