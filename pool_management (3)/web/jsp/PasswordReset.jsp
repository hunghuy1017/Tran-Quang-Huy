<!DOCTYPE html>
<html lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/PasswordReset.css">
<head>
    <meta charset="UTF-8">
    <title>Password Reset</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/ServletPasswordReset" method="POST">
    <div class="reset-container">
        <h2>Đặt lại mật khẩu</h2>
        <c:if test="${checkEmail != null}">
            <p style="color: red">${checkEmail}</p>
        </c:if>
            <c:if test="${mess != null}">
            <p style="color: red">${mess}</p>
        </c:if>
        <c:if test="${Wmess != null}">
            <p style="color: greenyellow">${Wmess}</p>
        </c:if>
        <input type="email" name="email" placeholder="Nhập email của bạn"/>
        <button type="submit" name="submit">Xác nhận</button>
        <div class="login-link">
            <a href="${pageContext.request.contextPath}/ServletUsers?service=loginUser">login</a>
        </div>
    </div>
    </form>
</body>
</html>
