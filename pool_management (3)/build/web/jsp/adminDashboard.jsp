<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/adminDashboard.css">
    <%-- Thư viện Chart.js cần thiết cho cả hai biểu đồ --%>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <jsp:include page="headerAdmin.jsp" /> 

    <div class="container">
        <h1>Bảng điều khiển Admin</h1>

        <div class="section">
            <h2>Thống kê tổng quan</h2>
            <div class="summary-grid">
                <div class="summary-card"><h4>Tổng người dùng</h4><p>${totalUsers}</p></div>
                <div class="summary-card"><h4>Tổng nhân viên</h4><p>${totalEmployees}</p></div>
                <div class="summary-card"><h4>Tổng sự kiện</h4><p>${totalEvents}</p></div>
                <div class="summary-card"><h4>Tổng đơn hàng</h4><p>${totalOrders}</p></div>
                <div class="summary-card"><h4>Doanh thu hôm nay</h4><p>${String.format("%,.0f VNĐ", todayRevenue)}</p></div>
            </div>
        </div>

        <div class="section">
            <h2>Người dùng đăng kí gần đây</h2>
            <form action="AdminDashboardServlet" method="get" class="filter-form">
                <div><label for="userName">Tên:</label><input type="text" id="userName" name="userName" value="${param.userName != null ? param.userName : ''}"></div>
                <div><label for="userDate">Ngày:</label><input type="date" id="userDate" name="userDate" value="${param.userDate != null ? param.userDate : ''}"></div>
                <button type="submit">Tìm kiếm</button>
            </form>
            <table>
                <thead><tr><th>ID</th><th>Tên</th><th>Email</th><th>SĐT</th><th>Ngày đăng kí</th></tr></thead>
                <tbody>
                    <c:choose><c:when test="${not empty recentUsers}">
                        <c:forEach var="user" items="${recentUsers}">
                            <tr>
                                <td>${user.userID}</td><td>${user.fullName}</td><td>${user.email}</td>
                                <td>${user.phone}</td><td>${user.createdAt}</td>
                            </tr>
                        </c:forEach>
                    </c:when><c:otherwise><tr><td colspan="5">Không tìm thấy người dùng nào.</td></tr></c:otherwise></c:choose>
                </tbody>
            </table>
            <div class="view-more"><a href="#">Xem thêm người dùng...</a></div>
        </div>

        <div class="section">
            <h2>Các huấn luyện viên được thuê nhiều nhất</h2>
            <form action="AdminDashboardServlet" method="get" class="filter-form">
                <div><label for="trainerName">Tên HLV:</label><input type="text" id="trainerName" name="trainerName" value="${param.trainerName != null ? param.trainerName : ''}"></div>
                <button type="submit">Tìm kiếm</button>
            </form>
            <table>
                <thead><tr><th>ID</th><th>Tên HLV</th><th>Email</th><th>Số lần thuê</th><th>Ngày thuê gần nhất</th></tr></thead>
                <tbody>
                    <c:choose><c:when test="${not empty topTrainers}">
                        <c:forEach var="trainer" items="${topTrainers}">
                            <tr>
                                <td>${trainer.userID}</td><td>${trainer.fullName}</td><td>${trainer.email}</td>
                                <td>${trainer.bookingCount}</td><td>${trainer.lastBookingDate}</td>
                            </tr>
                        </c:forEach>
                    </c:when><c:otherwise><tr><td colspan="5">Không tìm thấy HLV nào.</td></tr></c:otherwise></c:choose>
                </tbody>
            </table>
            <div class="view-more"><a href="#">Xem thêm HLV...</a></div>
        </div>

        <div class="section">
            <h2>Thông báo gần đây</h2>
            <ul class="notification-list">
                <c:choose><c:when test="${not empty recentNotifications}">
                    <c:forEach var="notif" items="${recentNotifications}">
                        <li><strong>${notif.title}:</strong> ${notif.message} (${notif.createdAt})</li>
                    </c:forEach>
                </c:when><c:otherwise><li>Không có thông báo gần đây.</li></c:otherwise></c:choose>
            </ul>
            <div class="view-more"><a href="#">Xem thêm thông báo...</a></div>
        </div>

        <div class="section">
            <h2>Doanh thu theo tháng</h2>
            <div class="chart-container">
                <canvas id="monthlyRevenueChart"></canvas>
            </div>
        </div>

        <div class="section">
            <h2>Đánh giá trung bình hồ bơi</h2>
            <div class="chart-container">
                <canvas id="poolRatingChart"></canvas>
            </div>
        </div>
    </div>

    <script>
        // --- SCRIPT CHO DOANH THU THEO THÁNG ---
        var monthlyLabels = [
            <c:forEach var="item" items="${monthlyRevenue}" varStatus="loop">
                'Tháng ${item.month}'<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];
        var monthlyData = [
            <c:forEach var="item" items="${monthlyRevenue}" varStatus="loop">
                ${item.revenue}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];

        var ctxMonthly = document.getElementById('monthlyRevenueChart').getContext('2d');
        var monthlyChart = new Chart(ctxMonthly, {
            type: 'bar',
            data: {
                labels: monthlyLabels,
                datasets: [{
                    label: 'Doanh thu (VNĐ)',
                    data: monthlyData,
                    backgroundColor: 'rgba(0, 123, 255, 0.5)',
                    borderColor: 'rgba(0, 123, 255, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: 'Doanh thu (VNĐ)' } },
                    x: { title: { display: true, text: 'Tháng' } }
                }
            }
        });

        // --- SCRIPT CHO ĐÁNH GIÁ TRUNG BÌNH HỒ BƠI ---
        var poolLabels = [
            <c:forEach var="item" items="${averagePoolRatings}" varStatus="loop">
                '${item.poolName}'<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];
        var poolRatingsData = [
            <c:forEach var="item" items="${averagePoolRatings}" varStatus="loop">
                ${item.averageRating}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];

        var ctxPool = document.getElementById('poolRatingChart').getContext('2d');
        var poolRatingChart = new Chart(ctxPool, {
            type: 'bar', // Có thể đổi thành 'scatter' hoặc 'line' nếu muốn hiển thị kiểu khác
            data: {
                labels: poolLabels,
                datasets: [{
                    label: 'Điểm đánh giá trung bình',
                    data: poolRatingsData,
                    backgroundColor: 'rgba(40, 167, 69, 0.5)', // Màu xanh lá
                    borderColor: 'rgba(40, 167, 69, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 5, // Điểm đánh giá từ 0 đến 5
                        title: { display: true, text: 'Điểm đánh giá (Sao)' }
                    },
                    x: {
                        title: { display: true, text: 'Tên hồ bơi' }
                    }
                }
            }
        });
    </script>
</body>
</html>