<%-- 
    Document   : LoginUser
    Created on : May 25, 2025, 12:22:32 AM
    Author     : Hi
--%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Notifications</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/LoginUser.css">
        

    </head>
    <body>

        

            <div class="col-md-6">
                <div class="detail-box">
                    <div class="heading_container">
                        <h2>Chào mừng tới BluePool</h2>
                    </div>
                    <div class="login-container">
                        <div class="login-form">
                            <form action="${pageContext.request.contextPath}/ServletUsers" method="POST">
                            <div class="form-group">
                                <label for="phone">Số điện thoại:</label>
                                <input type="text"  name="phone" placeholder="phone"  required>
                            </div>
                            <div class="form-group">
                                <label for="password">Mật khẩu</label>
                                <div style="position: relative;">
                                    <input type="password" id="password" name="password" placeholder="Password"  required>
                                    <i class="fa fa-eye" id="togglePassword" style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); cursor: pointer; color: black"></i>
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
                            </script>


                            <div class="form-group checkbox-group">
                               
                                <a href="${pageContext.request.contextPath}/jsp/PasswordReset.jsp" class="forget-password">Quên mật khẩu</a>
                            </div>
                            <div class="form-group button-group">
                                <input type="submit" name="submit" value="Đăng nhập">
                                <input type="reset" value="Làm mới">
                            </div>
                            <input type="hidden" name="service" value="loginUser">

                            <!-- Thông báo lỗi nếu có -->
                            <c:if test="${not empty mess}">
                                <p class="error-message" style="color: white">${mess}</p>
                            </c:if>

                            <div class="form-group">
                                <a href="<%= request.getContextPath() %>/jsp/insertUsers.jsp" class="signup-link">
                                    Đăng ký ngay
                                </a>
                            </div>
                        </form>
                        <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=61592249571-724t5desbn6hmgh39pm7ck2bkuiugpsd.apps.googleusercontent.com&redirect_uri=http://localhost:9999/QuangHuy/ServletGoogle&response_type=code&scope=openid%20email%20profile&access_type=offline&prompt=consent
                           " class="btn bsb-btn-2xl btn-outline-dark rounded-0 d-flex align-items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-google text-danger" viewBox="0 0 16 16">
                            <path d="M15.545 6.558a9.42 9.42 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.689 7.689 0 0 1 5.352 2.082l-2.284 2.284A4.347 4.347 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.792 4.792 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.702 3.702 0 0 0 1.599-2.431H8v-3.08h7.545z" />
                            </svg>
                            <span class="ms-2 fs-6 flex-grow-1">Continue with Google</span>
                        </a>
                    </div>


                </div>
            </div>
        </div>
       
    </body>
</html>


