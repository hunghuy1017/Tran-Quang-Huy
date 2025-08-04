<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Notifications</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/../css/notification.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Register.css">
        <meta name="google-signin-client_id" content="660427811001-hg8og7sdueko7dl9pn2vvo1gr0eci9u6.apps.googleusercontent.com">
        <style>
            .required-star {
                color: red;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        
            <div class="detail-box">
                <div class="heading_container">
                    <h2>Register</h2>
                </div>
                <div class="login-form">
                    <form action="${pageContext.request.contextPath}/ServletUsers" method="POST" autocomplete="off">
                    <div class="form-group">
                        <label for="firstName">Họ tên đệm <span class="required-star">*</span></label>
                        <input type="text" id="firstName" name="firstName" placeholder="firstName" required>
                    </div>
                    <div class="form-group">
                        <label for="lastName">Tên <span class="required-star">*</span></label>
                        <input type="text" id="lastName" name="lastName" placeholder="lastName" required>
                    </div>
                    <div class="form-group">
                        <label for="phone">Số Điện Thoại <span class="required-star">*</span></label>
                        <input type="text" id="phone" name="phone" placeholder="phone" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Mật khẩu <span class="required-star">*</span></label>
                        <div style="position: relative;">
                            <input type="password" id="password" name="password" placeholder="Password" required autocomplete="new-password">
                            <i class="fa fa-eye" id="togglePassword" style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); cursor: pointer;"></i>
                        </div>
                    </div>
                    <script>
                        document.getElementById("togglePassword").addEventListener("click", function () {
                            var password = document.getElementById("password");
                            var icon = this;
                            if (password.type === "password") {
                                password.type = "text";
                                icon.classList.remove("fa-eye");
                                icon.classList.add("fa-eye-slash");
                            } else {
                                password.type = "password";
                                icon.classList.remove("fa-eye-slash");
                                icon.classList.add("fa-eye");
                            }
                        });

                        document.addEventListener("DOMContentLoaded", function () {
                            document.getElementById("email").value = "";
                            document.getElementById("password").value = "";
                            document.getElementById("email").addEventListener("focus", function () {
                                this.value = "";
                            });
                            document.getElementById("password").addEventListener("focus", function () {
                                this.value = "";
                            });
                        });
                    </script>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="text" id="email" name="email" placeholder="email" autocomplete="off">
                    </div>
                    <div class="form-group">
                        <label for="address">Địa chỉ</label>
                        <input type="text" id="address" name="address" placeholder="address">
                    </div>
                    <div class="form-group button-group">
                        <input type="submit" name="submit" value="Đăng Ký">
                        <input type="button" value="Trở lại trang đăng nhập" onclick="window.location.href = '${pageContext.request.contextPath}/jsp/LoginUser.jsp'">
                    </div>
                    <input type="hidden" name="service" value="addUser">
                    <!-- Thông báo lỗi nếu có -->
                    <c:if test="${not empty mess}">
                        <p class="error-message" style="color: white">${mess}</p>
                    </c:if>
                </form>
            </div>
        </div>
        
    </body>
</html>