<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ConfirmReset.css">
<html lang="en">

<head>
  <meta charset="UTF-8">
  <title>Password Reset</title>

</head>
<body>

  <div class="container">
     <form action="${pageContext.request.contextPath}/ServletPasswordReset" method="POST">
    <h2>Đặt lại mật khẩu</h2> 
    <c:if test="${checkPassword != null}">
        <p style="color: red">${checkPassword}</p>
    </c:if>
    <input type="password" name="password" placeholder="Nhập mật khẩu mới" />
    <input type="password" name="confirmpassword" placeholder="Xác nhận lại mật khẩu" />
    <button type="submit">Xác nhận</button>
    <input type="hidden" name="service" value="ConfirmReset">
  </div>
  </form>
</body>
</html>
