<%-- 
    Document   : EventForm
    Created on : May 31, 2025, 11:56:16 PM
    Author     : Hi
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>BluePool - ${event.eventID == 0 ? 'Thêm' : 'Sửa'} Sự kiện</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/EventForm.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap">
    </head>
    <body>
        <header>
            <%@include file="header.jsp"%>
        </header>
        <div class="container">
            <h2>${event.eventID == 0 ? 'Thêm Sự kiện' : 'Sửa Sự kiện'}</h2>
            <c:if test="${not empty sessionScope.message}">
                <p style="color: green;">${sessionScope.message}</p>
                <c:remove var="message" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <p style="color: red;">${sessionScope.error}</p>
                <c:remove var="error" scope="session"/>
            </c:if>
            <form action="${pageContext.request.contextPath}/Event" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${event.eventID}">
                <div class="form-group">
                    <label for="title">Tiêu đề:</label>
                    <input type="text" id="title" name="title" value="${not empty param.title ? param.title : event.title}" required>
                </div>
                <div class="form-group">
                    <label for="description">Mô tả ngắn:</label>
                    <textarea id="description" name="description" required>${not empty param.description ? param.description : event.description}</textarea>
                </div>
                <div class="form-group">
                    <label for="detailedDescription">Mô tả chi tiết:</label>
                    <textarea id="detailedDescription" name="detailedDescription">${not empty param.detailedDescription ? param.detailedDescription : event.detailedDescription}</textarea>
                </div>
                <div class="form-group">
                    <label for="eventDate">Ngày bắt đầu:</label>
                    <input type="date" id="eventDate" name="eventDate" value="${not empty param.eventDate ? param.eventDate : (empty event.eventDate ? '' : fn:substring(event.eventDate, 0, 10))}" required>
                </div>
                <div class="form-group">
                    <label for="endDate">Ngày kết thúc:</label>
                    <input type="date" id="endDate" name="endDate" value="${not empty param.endDate ? param.endDate : (empty event.endDate ? '' : fn:substring(event.endDate, 0, 10))}">
                </div>
                <div class="form-group">
                    <label for="poolID">ID Hồ bơi:</label>
                    <input type="number" id="poolID" name="poolID" value="${not empty param.poolID ? param.poolID : event.poolID}" required>
                </div>
                <div class="form-group">
                    <label for="image">Hình ảnh:</label>
                    <input type="file" id="image" name="image" accept="image/*">
                    <c:if test="${not empty event.image}">
                        <p>Hình ảnh hiện tại: <img src="${pageContext.request.contextPath}/images/event/${event.image}" alt="Event Image" width="100"></p>
                    </c:if>
                </div>
                <button type="submit">Lưu</button>
            </form>
            <a href="${pageContext.request.contextPath}/Event" class="back-link">Quay lại</a>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>