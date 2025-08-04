<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/EmployeeDashboard.css">
</head>
<body>
    <%@include file="header.jsp"%>

    <h2>Dashboard Employee</h2>
    <div class="content">
        
        <a href="${pageContext.request.contextPath}/EmployeeProfile" class="card">Thông tin nhân viên</a>
        
        <c:if test="${position == 'Manager' || position == 'Warehouse Management'}">
            <a href="${pageContext.request.contextPath}/ServletWarehouseManager" class="card">Quản lý kho</a>
        </c:if>
     
        <c:if test="${position == 'Manager' || position == 'Seller'}">
            <a href="${pageContext.request.contextPath}/Seller" class="card">Bán Hàng</a>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/EmployeeSchedule" class="card">Lịch trình nhân viên</a>
     
        <c:if test="${position == 'Manager' || position == 'Seller'}">
            <a href="${pageContext.request.contextPath}/RentalManager" class="card">Quản lý thuê đồ</a>
        </c:if>
    </div>

    <%@include file="footer.jsp"%>
</body>
</html>