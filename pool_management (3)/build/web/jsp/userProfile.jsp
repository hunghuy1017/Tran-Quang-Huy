<%-- 
    Document   : userProflie
    Created on : May 22, 2025, 3:26:18 PM
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UserProfie Page</title>
    </head>
    <%@include file="../jsp/header.jsp" %>
    <!--<link rel="stylesheet" href="../header/header.css" />-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userProfile.css">

    <body>

        <div class="card-info">

            <form action="${pageContext.request.contextPath}/UserProfile" method="post" enctype="multipart/form-data">
                <div class="avatar">
<!--                    <img src="${pageContext.request.contextPath}/images/${empty user.image ? 'images/default-avatar.png' : user.image}" 
                         alt="Avatar" style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover;">-->

                    <c:choose>
                        <c:when test="${not empty user.image}">
                            <img src="${pageContext.request.contextPath}/images/${user.image}" 
                                 alt="Avatar" style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover;">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/images/default-avatar.png" 
                                 alt="Avatar" style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover;">
                        </c:otherwise>
                    </c:choose>


                    <!-- Hidden file input -->
                    <input type="file" name="avatar" id="avatarInput" style="display: none;" accept="image/*">

                    <!-- Styled label as button -->
                    <label for="avatarInput" class="button" style="margin-left: 12px; cursor: pointer;">
                        Update Image
                    </label>

                    <!-- Hidden submit button -->
                    <input type="submit" id="uploadBtn" style="display: none;">
                </div>
                <h2>Thông Tin</h2>

                 <div class="form-group">
                    <label>Họ và Tên</label>
                    <input type="text" value="${user.fullName}" name="fullName" readonly id="fullNameInput" >
                    <div class="action"><a onclick="enableEditing('fullNameInput')">edit</a></div>
                </div>

                <div class="form-group">
                    <label>Mật Khẩu</label>
                    <div class="input-group">
                        <input type="password" value="${user.password}" id="passwordField" name="password"readonly>
                        <span class="eye-icon" onclick="togglePassword()">👁</span>
                    </div>
                    <div class="action"><a href="ChangPassword">Thay đổi mật khẩu</a></div>
                </div>

              

                <div class="form-group">
                    <label>Email</label>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.email}">
                            <input type="email" value="${user.email}" name="email" readonly id="emailInput">
                            <div class="action"><a onclick="enableEditingEmail('emailInput')">Sửa email</a></div>
                        </c:when>
                        <c:otherwise>
                            <input type="text" value="Bạn chưa thêm Email" name="email" readonly style="color: red;">
                            <div class="action"><a onclick="enableEditingEmail('emailInput')">Thêm Email</a></div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="form-group">
                    <label>Số Điện Thoại</label>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.phone}">
                    <input type="text" value="${user.phone}"name="phone" readonly id="sdtInput">
                    <div class="action"><a onclick="enableEditingSdt('sdtInput')">sửa sdt</a></div>
                     </c:when>
                        <c:otherwise>
                        <input type="text" value="Bạn chưa thêm sô điện thoại"name="phone" readonly id="sdtInput">
                    <div class="action"><a onclick="enableEditingSdt('sdtInput')">Thêm sđt</a></div>
                           </c:otherwise>
                    </c:choose>
                </div>
             
                <div class="form-group">
                    <label>Huấn Luyện Viên</label>
                    <c:choose>
                        <c:when test="${not empty uT}">
                            <input type="text" value="${uT.fullName}" readonly>
                            <div class="action">
                                <a href="#">Xem chi tiết</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <input type="text" value="Bạn chưa có trainer" readonly style="color: red;">
                            <div class="action">
                                <a href="#">Đặt lịch huấn luyện viên</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:choose>
                    <c:when test="${not empty uPack}">
                        <div class="form-group">
                            <label>Ngày bắt đầu</label>
                            <input type="text" value="${uPack.startDate}" readonly>
                            <div class="action"></div>
                        </div>

                        <div class="form-group">
                            <label>Ngày kết thúc</label>
                            <input type="text" value="${uPack.endTime}" readonly>
                            <div class="action"></div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="form-group">
                            <label>Ngày bắt đầu</label>
                            <input type="text" value="Bạn chưa có gói bơi" readonly style="color: red">
                            <div class="action"><a href="Package">Buy Package</a></div>
                        </div>
                    </c:otherwise>
                </c:choose>
                        <c:if test="${not empty mess}" >
                            <div style="color: red">${mess}</div>
                        </c:if>
        </div>
        <div class="button"><input type="submit" value="Lưu thay đổi"></div>
    </form>



    <%@include file="../jsp/footer.jsp" %>
    <script>
        function togglePassword() {
            const pwField = document.getElementById("passwordField");
            pwField.type = pwField.type === "password" ? "text" : "password";
        }
    </script>

    <script>
        const avatarInput = document.getElementById("avatarInput");
        const avatarImg = document.querySelector(".avatar img");

        avatarInput.addEventListener("change", function () {
            const file = this.files[0];
            if (file) {
                avatarImg.src = URL.createObjectURL(file);
            }
        });
    </script>
    <script>
        function enableEditingSdt(inputId) {
            const input = document.getElementById(inputId);
            input.readOnly = false;
            input.focus(); // Tự động focus vào input
            input.style.border = "1px solid #ccc"; // Optional: thêm viền để nhìn thấy rõ
        }
        function enableEditing(inputId) {
            const input = document.getElementById(inputId);
            input.readOnly = false;
            input.focus(); // Tự động focus vào input
            input.style.border = "1px solid #ccc"; // Optional: thêm viền để nhìn thấy rõ
        }
        function enableEditingEmail(inputId) {
            const input = document.getElementById(inputId);
            input.readOnly = false;
            input.focus(); // Tự động focus vào input
            input.style.border = "1px solid #ccc"; // Optional: thêm viền để nhìn thấy rõ
        }
        function enableEditingAddress(inputId) {
            const input = document.getElementById(inputId);
            input.readOnly = false;
            input.focus(); // Tự động focus vào input
            input.style.border = "1px solid #ccc"; // Optional: thêm viền để nhìn thấy rõ
        }
    </script>

</body>
</html>
