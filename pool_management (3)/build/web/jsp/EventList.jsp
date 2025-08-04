<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="models.Event"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>BluePool - Danh sách Sự kiện</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/EventList.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Home1.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap">
    </head>
    <body>
        
        <%@include file="header.jsp"%>
       
        <div class="container">
            <c:if test="${sessionScope.roleID == 1}">
                <div class="add-event-container">
                    <a href="${pageContext.request.contextPath}/Event?action=add" class="add-event">+ Thêm Sự kiện</a>
                    <a href="${pageContext.request.contextPath}/Event?action=registrationList" class="add-event">Quản lý đăng ký sự kiện</a>

                </div>
            </c:if>
            <c:if test="${not empty sessionScope.message}">
                <p style="color: green;">${sessionScope.message}</p>
                <c:remove var="message" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <p style="color: red;">${sessionScope.error}</p>
                <c:remove var="error" scope="session"/>
            </c:if>
            

            <!-- Form tìm kiếm -->
            <h2 class="section-title">🎉 Sự Kiện</h2>
            <!-- Form tìm kiếm -->
            <form action="Event" method="get" class="search-form">
                <input type="text" name="eventSearch" placeholder="Search events..." value="${eventSearch}">
                <label for="eventMonth">Tháng:</label>
                <select name="eventMonth" id="eventMonth">
                    <option value="">All</option>
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
                    <option value="">All</option>
                    <% 
                        String selectedYear = request.getParameter("eventYear");
                        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                        for(int y = currentYear; y >= currentYear - 5; y--) { 
                            String val = String.valueOf(y);
                    %>
                    <option value="<%=val%>" <%= val.equals(selectedYear) ? "selected" : "" %>><%= y %></option>
                    <% } %>
                </select>
                <button type="submit">Tìm Kiếm</button>
            </form>


                <!-- Danh sách sự kiện -->
                <ul class="event-list">

                    <c:choose>
                        <c:when test="${empty events}">
                            <li>Không tìm thấy sự kiện nào.</li>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="e" items="${events}">
                                <li>
                                    <c:if test="${not empty e.image}">
                                        <img src="${pageContext.request.contextPath}/images/event/${e.image}" alt="Event Image">
                                    </c:if>
                                    <div class="events">
                                        <div class="event-content">
                                            <strong>${e.title}</strong>
                                            <em>${e.eventDate}</em>
                                            <p>${e.description}</p>
                                        </div>
                                        <div class="action-links">
                                            <a href="${pageContext.request.contextPath}/Event?action=EventDetail&id=${e.eventID}" class="btn-view">Xem</a>
                                            <c:if test="${sessionScope.roleID == 1}">
                                                <a href="${pageContext.request.contextPath}/Event?action=delete&id=${e.eventID}" class="btn-view" onclick="return confirm('Bạn có chắc muốn xóa sự kiện này?')">Xóa</a>
                                            </div>
                                        </c:if>
                                    </div>

                                </li>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </ul>

                <!-- Phân trang -->
            <div class="pagination">
                <c:if test="${totalPages > 1}">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/Event?page=${currentPage - 1}&eventSearch=${eventSearch}&eventMonth=${eventMonth}&eventYear=${eventYear}" class="page-link">Previous</a>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="${pageContext.request.contextPath}/Event?page=${i}&eventSearch=${eventSearch}&eventMonth=${eventMonth}&eventYear=${eventYear}" class="page-link ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/Event?page=${currentPage + 1}&eventSearch=${eventSearch}&eventMonth=${eventMonth}&eventYear=${eventYear}" class="page-link">Next</a>
                    </c:if>
                </c:if>
            </div>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>