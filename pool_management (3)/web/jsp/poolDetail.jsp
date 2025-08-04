<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Chi tiết hồ bơi</title>
    <link rel="stylesheet" href="css/poolDetail.css">
</head>
<body>
    <header>
        <jsp:include page="header.jsp"></jsp:include>
    </header>

    <h2>Chi tiết hồ bơi</h2>

    <c:if test="${not empty pool}">
        <div class="container">
            <div class="left">
                <img src="${not empty pool.image ? 'images/pools/' += pool.image : 'images/default-pool.jpg'}" 
                     alt="${pool.name}" 
                     onerror="this.src='images/default-pool.jpg'">
            </div>
            <div class="right">
                <p><strong>Tên:</strong> ${pool.name}</p>
                <p><strong>Địa điểm:</strong> ${pool.location}</p>
                <p><strong>Điện thoại:</strong> ${not empty pool.phone ? pool.phone : 'Not updated'}</p>
                <p><strong>Trang fanpage:</strong> 
                    <c:choose>
                        <c:when test="${not empty pool.fanpage}">
                            <a href="${pool.fanpage}" target="_blank" rel="noopener noreferrer">${pool.fanpage}</a>
                        </c:when>
                        <c:otherwise>Not updated</c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Giờ mở cửa:</strong> ${not empty pool.openTime ? pool.openTime : 'Not updated'}</p>
                <p><strong>Giờ đóng cửa:</strong> ${not empty pool.closeTime ? pool.closeTime : 'Not updated'}</p>
                <p><strong>Mô tả:</strong> ${not empty pool.description ? pool.description : 'No description available'}</p>
                <p><strong>Trạng thái:</strong> 
                    <span class="${pool.status ? 'status-active' : 'status-inactive'}">
                        <c:choose>
                            <c:when test="${pool.status}">Đang hoạt động</c:when>
                            <c:otherwise>Ngưng hoạt động</c:otherwise>
                        </c:choose>
                    </span>
                </p>
            </div>
        </div>
    </c:if>
    <c:if test="${empty pool}">
        <p>No pool found with the specified ID.</p>
    </c:if>

    <div class="section">
        <h3>📅 Sự kiện <a href="${pageContext.request.contextPath}/Event?page=1" class="view-btn">Xem</a></h3>
        <c:choose>
            <c:when test="${not empty events}">
                <ul>
                    <c:forEach var="e" items="${events}">
                        <li>
                            <strong>${e.title}</strong> – ${e.eventDate} 
                            <br>${e.description}
                            <br><img src="images/event/${e.image}" alt="${e.title}" style="width: 100px; height: auto; border-radius: 8px;" onerror="this.src='images/default-event.jpg'">
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p>Không có sự kiện nào có sẵn cho hồ bơi này.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="section">
        <h3>👨‍🏫 Đặt chỗ huấn luyện viên <a href="${pageContext.request.contextPath}/SwimmingPoolDetailServlet?id=${pool.poolID}#trainer-bookings" class="view-btn">Xem</a></h3>
        <c:choose>
            <c:when test="${not empty bookings}">
                <ul>
                    <c:forEach var="b" items="${bookings}">
                        <li>${b.userName} – ${b.bookingDate} – ${b.note}</li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p>Không có huấn luyện viên nào có thể đặt chỗ cho hồ bơi này.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="section">
        <h3>⭐ Đánh giá của người dùng <a href="${pageContext.request.contextPath}/ServletUserReview?service=listUserReview&poolID=${pool.poolID}" class="view-btn">Xem</a></h3>
        <c:choose>
            <c:when test="${not empty reviews}">
                <ul>
                    <c:forEach var="r" items="${reviews}">
                        <li>
                            <span class="rating">
                                <c:forEach var="i" begin="1" end="5">
                                    <span class="star ${i <= r.rating ? 'filled' : ''}">★</span>
                                </c:forEach>
                            </span> 
                            – "${r.comment}" – Created on: ${r.createdAt}
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p>Không có đánh giá nào của người dùng cho hồ bơi này.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="section">
        <h3>🏊 Các hồ bơi khác</h3>
        <c:choose>
            <c:when test="${not empty otherPools}">
                <div class="other-pools-container">
                    <c:forEach var="otherPool" items="${otherPools}">
                        <div class="other-pool-card">
                            <img src="${not empty otherPool.image ? 'images/pools/' += otherPool.image : 'images/default-pool.jpg'}" 
                                 alt="${otherPool.name}" 
                                 onerror="this.src='images/default-pool.jpg'">
                            <p><a href="SwimmingPoolDetailServlet?id=${otherPool.poolID}">${otherPool.name}</a></p>
                            <p>
                                <span class="${otherPool.status ? 'status-active' : 'status-inactive'}">
                                    ${otherPool.status ? 'Đang hoạt động' : 'Ngưng hoạt động'}
                                </span>
                            </p>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p>Không có hồ bơi nào khác.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>