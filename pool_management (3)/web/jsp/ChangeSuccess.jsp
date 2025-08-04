<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ChangeSuccess.css">
<html>
<head>
    <meta charset="UTF-8">
    <title>Password Changed</title>
    
</head>
<body>
    <div class="container">
        <div class="message">Thay đổi mật khẩu<br>Thành công</div>
        <a href="${pageContext.request.contextPath}/jsp/LoginUser.jsp" class="button">Đi đến đăng nhập</a>
    </div>
</body>
</html>
