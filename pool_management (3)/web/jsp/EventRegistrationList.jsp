<%-- 
    Document   : RegisterEvent
    Created on : Jun 23, 2025, 8:50:58 AM
    Author     : Hi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/EventRegister.css">
    </head>
    <body>
        <jsp:include page="headerAdmin.jsp" /> 
       <h3>Danh sách đăng ký tham gia sự kiện</h3>

        <c:if test="${not empty message}">
            <div class="message">${message}</div>
            <c:remove var="message" scope="session"/>
        </c:if>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <form method="get" action="Event" class="search-form">
            <input type="hidden" name="action" value="registrationList">
            Tên sự kiện: <input type="text" name="searchEvent" value="${searchEvent}">
            Số điện thoại: <input type="text" name="searchPhone" value="${searchPhone}">
            Tên bể bơi: <input type="text" name="searchPool" value="${searchPool}">
            Trạng thái:
            <select name="status">
                <option value="">Tất cả</option>
                <option value="Pending" ${status == 'Pending' ? 'selected' : ''}>Pending</option>
                <option value="Approved" ${status == 'Approved' ? 'selected' : ''}>Approved</option>
                <option value="Rejected" ${status == 'Rejected' ? 'selected' : ''}>Rejected</option>
            </select>
            
            <button type="submit">Tìm kiếm</button>
        </form>

        <table>
            <tr>
                <th>Sự kiện</th>
                <th>Bể bơi</th>
                <th>Họ tên</th>
                <th>Điện thoại</th>
                <th>Email</th>
                <th>Ngày đăng ký</th>
                
                <c:if test="${sessionScope.roleID == 1}">
                    <th>Trạng thái</th>
                </c:if>
                    
            </tr>
            <c:forEach var="reg" items="${registrations}">
                <tr>
                    <td>${reg.eventTitle}</td>
                    <td>${reg.poolName}</td>
                    <td>${reg.fullName}</td>
                    <td>${reg.phone}</td>
                    <td>${reg.email}</td>
                    <td>${reg.registeredAt}</td>
                    
                    <c:if test="${sessionScope.roleID == 1}">
                        <td>
                            <form method="post" action="Event" class="action-form">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="registrationID" value="${reg.registrationID}">
                                <input type="hidden" name="searchEvent" value="${searchEvent}">
                                <input type="hidden" name="searchPhone" value="${searchPhone}">
                                <input type="hidden" name="searchPool" value="${searchPool}">
                                <input type="hidden" name="status" value="${status}">
                                <select name="newStatus">
                                    <option value="Pending" ${reg.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                    <option value="Approved" ${reg.status == 'Approved' ? 'selected' : ''}>Approved</option>
                                    <option value="Rejected" ${reg.status == 'Rejected' ? 'selected' : ''}>Rejected</option>
                                </select>
                                <button type="submit">Cập nhật</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            <c:if test="${empty registrations}">
                <tr>
                    <td colspan="${sessionScope.roleID == 1 ? 8 : 7}" class="no-data">Không có bản ghi nào được tìm thấy.</td>
                </tr>
            </c:if>
        </table>
            <div class="action-links">
                <a href="${pageContext.request.contextPath}/Event" class="back">Quay lại</a>
            </div>
            
    </body>
</html>
