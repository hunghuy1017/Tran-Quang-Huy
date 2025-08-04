<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Employee" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Danh sách Huấn luyện viên</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/trainerList.css">

    </head>
    <body>
        <jsp:include page="header.jsp" />
        <h2 class="section-title">🏅 Danh sách Huấn luyện viên</h2>

        <form action="trainer-list" method="get" class="search-form">
            <input type="text" name="trainerSearch" placeholder="Tìm kiếm huấn luyện viên..."
                   value="<%= request.getParameter("trainerSearch") != null ? request.getParameter("trainerSearch") : "" %>">
            <button type="submit">Tìm kiếm</button>
        </form>

        <div class="trainer-grid">
            <%
                List<Employee> trainerList = (List<Employee>) request.getAttribute("trainerList");
                if (trainerList == null || trainerList.isEmpty()) {
            %>
            <p class="no-trainer">Chưa có huấn luyện viên nào.</p>
            <%
                } else {
                    for (Employee t : trainerList) {
            %>
            <div class="trainer-card">
                <img src="<%= (t.getImage() != null && !t.getImage().isEmpty()) 
                            ? "images/trainer/" + t.getImage() 
                            : "images/default-avatar.jpg" %>" 
                     alt="Huấn luyện viên">

                <div class="trainer-info">
                    <p><strong>Họ tên:</strong> <%= t.getFullName() %></p>
                    <p><strong>Email:</strong> <%= t.getEmail() != null ? t.getEmail() : "Chưa cập nhật" %></p>
                    <p><strong>Số điện thoại:</strong> <%= t.getPhone() != null ? t.getPhone() : "Chưa cập nhật" %></p>
                    <p><strong>Hồ bơi:</strong> <%= t.getPoolName() != null ? t.getPoolName() : "Chưa rõ" %></p>
                    <p><strong>Lương theo giờ:</strong> <%= t.getHourlyRate() != null ? t.getHourlyRate() + " VNĐ" : "Chưa có" %></p>
                    <p><strong>Mô tả:</strong> <%= t.getDescription() != null ? t.getDescription() : "Chưa có mô tả" %></p>

                    <form action="book-trainer" method="get">
                        <input type="hidden" name="trainerID" value="<%= t.getUserID() %>">
                        <button type="submit">Đặt lịch ngay</button>
                    </form>
                </div>
            </div>
            <%
                    }
                }
            %>
        </div>

    </body>
</html>
