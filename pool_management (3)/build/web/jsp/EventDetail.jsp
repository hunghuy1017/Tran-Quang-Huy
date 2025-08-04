<%-- 
    Document   : EventDetail
    Created on : May 27, 2025, 10:07:06 PM
    Author     : Hi
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>BluePool - Sự Kiện</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/EventList.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Home1.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/EventDetail.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap">
    </head>
    <body>

        <%@include file="header.jsp"%>

        <div class="container">
            <c:choose>
                <c:when test="${empty event}">
                    <p class="error-message">Không tìm thấy sự kiện!</p>
                    <a href="${pageContext.request.contextPath}/Event" class="action-links back">Quay lại danh sách sự kiện</a>
                </c:when>
                <c:otherwise>
                    <div class="event-detail-container">
                        <h2>${event.title}</h2>
                        <p class="event-date">Ngày diễn ra:
                            <fmt:formatDate value="${event.eventDate}" pattern="dd/MM/yyyy"/> -
                            <fmt:formatDate value="${event.endDate}" pattern="dd/MM/yyyy"/>
                        </p>




                        <c:if test="${not empty event.image}">
                            <img src="${pageContext.request.contextPath}/images/event/${event.image}" alt="Hình ảnh sự kiện">
                        </c:if>

                        <c:choose>
                            <c:when test="${not empty event.detailedDescription}">
                                <div class="description-content">${event.detailedDescription}</div>
                            </c:when>
                            <c:otherwise>
                                Chưa có mô tả chi tiết.
                            </c:otherwise>
                        </c:choose>



                        <!-- Form đăng ký sự kiện -->
                        <c:if test="${not empty sessionScope.userID}">
                            <div class="registration-form">

                                <h3>Đăng ký tham gia sự kiện</h3>

                                <c:if test="${not empty sessionScope.errorMessage}">
                                    <p class="error-message">${sessionScope.errorMessage}</p>
                                    <c:remove var="errorMessage" scope="session"/>
                                </c:if>

                                <c:if test="${not empty sessionScope.successMessage}">
                                    <p class="success-message">${sessionScope.successMessage}</p>
                                    <c:remove var="successMessage" scope="session"/>
                                </c:if>



                                <form action="${pageContext.request.contextPath}/Event?action=register" method="Post">
                                    <input type="hidden" name="eventID" value="${event.eventID}">
                                    <input type="hidden" name="userID" value="${sessionScope.userID}">

                                    <label for="fullName">Họ và tên:</label>
                                    <input type="text" id="fullName" name="fullName" required placeholder="Nhập họ và tên">

                                    <label for="phone">Số điện thoại:</label>
                                    <input type="tel" id="phone" name="phone" required placeholder="Nhập số điện thoại">

                                    <label for="email">Email:</label>
                                    <input type="email" id="email" name="email" required placeholder="Nhập email">

                                    <label for="address">Địa chỉ:</label>
                                    <input type="text" id="address" name="address" required placeholder="Nhập địa chỉ">

                                    <label for="note">Ghi chú (nếu có):</label>
                                    <textarea id="note" name="note" rows="3" placeholder="Nhập ghi chú (không bắt buộc)"></textarea>

                                    <button type="submit">Đăng ký</button>
                                </form>


                            </div>
                            <div class="action-links">
                                <a href="${pageContext.request.contextPath}/Event" class="back">Quay lại</a>
                                <c:if test="${sessionScope.roleID == 1}">
                                    <a href="${pageContext.request.contextPath}/Event?action=edit&id=${event.eventID}" class="edit">Sửa</a>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>

</html>
