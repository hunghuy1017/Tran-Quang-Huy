<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Event"%>
<%@page import="models.SwimmingPool"%>
<%@page import="models.Employee"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Trang chủ - Hồ bơi</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Home1.css">
    </head>
    <body>

        <%@include file="header.jsp"%>

        <div class="banner">
            <img src="images/banner.png" alt="Banner">
        </div>

        <main class="section-container">

            <!-- 🎉 Sự kiện -->
            <h2 class="section-title">🎉 Sự kiện</h2>
            <form action="home" method="get" class="search-form">
                <input type="text" name="eventSearch" placeholder="Tìm kiếm sự kiện..." value="<%= request.getParameter("eventSearch") != null ? request.getParameter("eventSearch") : "" %>">

                <label for="eventMonth">Tháng:</label>
                <select name="eventMonth" id="eventMonth">
                    <option value="">Tất cả</option>
                    <% 
                        String selectedMonth = request.getParameter("eventMonth");
                        for(int m=1; m<=12; m++) { 
                            String val = String.format("%02d", m);
                    %>
                    <option value="<%=val%>" <%= val.equals(selectedMonth) ? "selected" : "" %>><%= m %></option>
                    <% } %>
                </select>

                <label for="eventYear">Năm:</label>
                <select name="eventYear" id="eventYear">
                    <option value="">Tất cả</option>
                    <% 
                        String selectedYear = request.getParameter("eventYear");
                        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                        for(int y = currentYear - 5; y <= currentYear + 5; y++) { 
                            String val = String.valueOf(y);
                    %>
                    <option value="<%=val%>" <%= val.equals(selectedYear) ? "selected" : "" %>><%= y %></option>
                    <% } %>
                </select>

                <button type="submit">Tìm kiếm</button>
            </form>

            <div class="event-container">
                <%
                    List<Event> eventList = (List<Event>) request.getAttribute("eventList");
                    if(eventList != null && !eventList.isEmpty()) {
                        for (Event e : eventList) {
                %>
                <div class="event-card">
                    <img src="images/event/<%= e.getImage() %>" alt="<%= e.getTitle() %>" class="event-img">
                    <div class="event-info">
                        <h3><%= e.getTitle() %></h3>
                        <p><strong>Ngày:</strong> <%= e.getEventDate() %></p>
                        <p><strong>Mô tả:</strong> <%= e.getDescription() %></p>
                        <div class="event-actions">
                            <a href="${pageContext.request.contextPath}/Event" class="btn-view">Xem</a>
                        </div>
                    </div>
                </div>
                <%
                        }
                    } else {
                %>
                <p>Không tìm thấy sự kiện nào.</p>
                <%
                    }
                %>
            </div>
            <div class="see-more-container">
                <a href="${pageContext.request.contextPath}/Event" class="see-more-link">Xem thêm &rarr;</a>
            </div>

            <!-- 🏊 Hồ bơi -->
            <h2 class="section-title">🏊 Hồ bơi</h2>
            <form action="home" method="get" class="search-form">
                <input type="text" name="poolSearch" placeholder="Tìm kiếm hồ bơi..." value="<%= request.getParameter("poolSearch") != null ? request.getParameter("poolSearch") : "" %>">
                <select name="poolStatus">
                    <option value="">Tất cả trạng thái</option>
                    <option value="active" <%= "active".equals(request.getParameter("poolStatus")) ? "selected" : "" %>>Đang hoạt động</option>
                    <option value="inactive" <%= "inactive".equals(request.getParameter("poolStatus")) ? "selected" : "" %>>Ngừng hoạt động</option>
                </select>
                <button type="submit">Lọc</button>
            </form>
            <%
                List<SwimmingPool> poolList = (List<SwimmingPool>) request.getAttribute("poolList");
                if (poolList != null && !poolList.isEmpty()) {
                    for (SwimmingPool sp : poolList) {
            %>
            <div class="pool-card">
                <img src="<%= (sp.getImage() != null && !sp.getImage().isEmpty()) ? "images/pools/" + sp.getImage() : "images/default-pool.jpg" %>" 
                     alt="<%= sp.getName() %>" class="pool-image">
                <div class="pool-info">
                    <h3 class="pool-name"><%= sp.getName() %></h3>
                    <p><strong>Địa điểm:</strong> <%= sp.getLocation() %></p>
                    <p><strong>Số điện thoại:</strong> <%= (sp.getPhone() != null) ? sp.getPhone() : "Chưa cập nhật" %></p>
                    <p>
                        <strong>Trạng thái:</strong>
                        <span class="<%= sp.isStatus() ? "status-active" : "status-inactive" %>">
                            <%= sp.isStatus() ? "Đang hoạt động" : "Ngừng hoạt động" %>
                        </span>
                    </p>
                </div>
                <div class="pool-actions">
                    <a href="SwimmingPoolDetailServlet?id=<%= sp.getPoolID() %>" class="btn-view">Xem</a>
                    <a href="ServletUserReview?service=listUserReview&poolID=<%= sp.getPoolID() %>" class="btn-rating">Đánh giá</a>
                </div>
            </div>
            <%
                    }
                } else {
            %>
            <p>Không tìm thấy hồ bơi nào.</p>
            <%
                }
            %>

            <!-- 🏅 Huấn luyện viên -->
            <h2 class="section-title">🏅 Huấn luyện viên</h2>
            <form action="home" method="get" class="search-form">
                <input type="text" name="trainerSearch" placeholder="Tìm kiếm huấn luyện viên..." value="<%= request.getParameter("trainerSearch") != null ? request.getParameter("trainerSearch") : "" %>">
                <button type="submit">Tìm kiếm</button>
            </form>

            <div class="trainer-grid">
                <%
                    List<Employee> trainerList = (List<Employee>) request.getAttribute("trainerList");
                    if (trainerList == null || trainerList.isEmpty()) {
                %>
                <p>Chưa có huấn luyện viên nào.</p>
                <%
                    } else {
                        for (Employee t : trainerList) {
                %>
                <div class="trainer-card">
                    <img src="<%= (t.getImage() != null && !t.getImage().isEmpty()) ? "images/trainer/" + t.getImage() : "images/default-avatar.jpg" %>" alt="Huấn luyện viên" class="trainer-img">
                    <div class="trainer-info">
                        <p><strong>Họ tên:</strong> <%= t.getFullName() %></p>
                        <p><strong>Số điện thoại:</strong> <%= t.getPhone() %></p>
                    </div>
                    <div class="trainer-actions">
                        <a href="trainerProfile.jsp?id=<%= t.getUserID() %>" class="btn-view">Xem</a>
                        <a href="bookTrainer.jsp?id=<%= t.getUserID() %>" class="btn-book">Đặt lịch</a>
                        <a href="ServletTrainerReview?service=listUserReviewTrainer&TrainerID=<%= t.getUserID()%>" class="btn-rating">Đánh giá</a>
                    </div>
                </div>
                <%
                        }
                    }
                %>
            </div>

        </main>

        <%@include file="footer.jsp"%>

    </body>
</html>
