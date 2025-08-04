<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="models.Employee" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    Employee emp = (Employee) request.getAttribute("employee");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Hồ sơ nhân viên</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/EmployeeProfile.css">
        <script>
            function enableFieldEdit(id) {
                const input = document.getElementById(id);
                input.removeAttribute("readonly");
                input.focus();
            }

            function validateForm() {
                const phone = document.getElementById("phone").value.trim();
                const fullName = document.getElementById("fullName").value.trim();

                const phoneRegex = /^[0-9]{10}$/;
                const nameRegex = /^[^\d]+$/;

                if (!phoneRegex.test(phone)) {
                    alert("⚠️ Số điện thoại phải đúng 10 chữ số!");
                    return false;
                }

                if (!nameRegex.test(fullName)) {
                    alert("⚠️ Họ tên không được chứa số!");
                    return false;
                }

                return true;
            }
        </script>
    </head>
    <body>
        <%@include file="header.jsp"%>

        <div class="profile-container">
            <!-- Avatar -->
            <div class="avatar">
                <form action="UploadAvatar" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="userID" value="${employee.userID}" />
                    <c:choose>
                        <c:when test="${not empty employee.image}">
                            <img src="${pageContext.request.contextPath}/${employee.image}" alt="Avatar"
                                 style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover;">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/images/default-avatar.png" alt="Avatar mặc định"
                                 style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover;">
                        </c:otherwise>
                    </c:choose>
                    <input type="file" name="avatar" id="avatarInput" style="display: none;" accept="images/*"
                           onchange="this.form.submit();">
                    <label for="avatarInput" class="button" style="margin-left: 12px; cursor: pointer;">📷 Cập nhật ảnh</label>
                </form>
            </div>

            <!-- Thông tin -->
            <div class="right-panel">
                <h2>🧾 <i>Thông tin nhân viên</i></h2>
                <form action="EmployeeProfile" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
                    <div class="form-group">
                        <label>Họ và tên:</label>
                        <input id="fullName" type="text" name="fullName"
                               value="${employee.fullName}" readonly
                               oninput="this.value = this.value.replace(/[0-9]/g, '');">
                        <button type="button" class="edit-btn" onclick="enableFieldEdit('fullName')">✏️ Sửa</button>
                    </div>

                    <div class="form-group">
                        <label>Email:</label>
                        <input id="email" type="text" name="email"
                               value="${employee.email}" readonly
                               oninput="this.value = this.value.trim();">
                        <button type="button" class="edit-btn" onclick="enableFieldEdit('email')">✏️ Sửa</button>
                    </div>


                    <div class="form-group">
                        <label>Số điện thoại:</label>
                        <input id="phone" type="text" name="phone"
                               value="${employee.phone}" readonly maxlength="10"
                               oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10);">
                        <button type="button" class="edit-btn" onclick="enableFieldEdit('phone')">✏️ Sửa</button>
                    </div>

                    <div class="form-group">
                        <label>Địa chỉ:</label>
                        <input id="address" type="text" name="address"
                               value="${employee.address}" readonly>
                        <button type="button" class="edit-btn" onclick="enableFieldEdit('address')">✏️ Sửa</button>
                    </div>

                    <div class="form-group">
                        <label>Chức vụ:</label>
                        <input type="text" value="Nhân viên" readonly>
                    </div>

                    <div class="form-group">
                        <label>Lương:</label>
                        <input type="text" value="${employee.salary} VNĐ" readonly>
                    </div>

                    <div class="form-group">
                        <label>Ngày bắt đầu:</label>
                        <input type="text" value="${employee.startDate}" readonly>
                    </div>

                    <div class="form-group">
                        <label>Mô tả công việc:</label>
                        <input type="text" value="${employee.description}" readonly>
                    </div>

                    <div class="form-group">
                        <label>Nơi làm việc:</label>
                        <input type="text" value="${employee.poolName}" readonly>
                    </div>

                    <input type="hidden" name="userID" value="${employee.userID}">
                    <div class="form-actions">
                        <button type="submit">💾 Lưu thay đổi</button>
                    </div>
                </form>

                <!-- Đổi mật khẩu -->
                <div class="change-password" style="margin-top: 20px;">
                    <form action="ChangPassword" method="get">
                        <input type="hidden" name="id" value="${employee.userID}">
                        <button type="submit">🔒 Đổi mật khẩu</button>
                    </form>
                </div>
                <c:if test="${not empty mess}">
                    <div class="message success">${mess}</div>
                </c:if>
            </div>
        </div>


    </body>
</html>