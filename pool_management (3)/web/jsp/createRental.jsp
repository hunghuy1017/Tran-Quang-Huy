<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tạo đơn thuê đồ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/rentalManager.css">
</head>
<body>
    <%@include file="header.jsp"%>

    <div class="container">
        <h2>Tạo đơn thuê đồ mới</h2>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <form action="RentalManager" method="post">
            <input type="hidden" name="action" value="createRental">

            <div class="form-group">
                <label for="customerName">Tên khách hàng</label>
                <input type="text" id="customerName" name="customerName" required placeholder="Nhập tên khách hàng">
            </div>

            <div class="form-group">
                <label for="phoneNumber">Số điện thoại</label>
                <input type="tel" id="phoneNumber" name="phoneNumber" required placeholder="Nhập số điện thoại (10 số)">
            </div>

            <div class="form-group">
                <label for="productID">Sản phẩm</label>
                <select id="productID" name="productID" required>
                    <option value="">Chọn sản phẩm</option>
                    <c:forEach var="product" items="${availableProducts}">
                        <option value="${product.productID}">${product.productName} (VND ${product.rentalPrice}/giờ)</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="startDate">Thời gian bắt đầu</label>
                <input type="datetime-local" id="startDate" name="startDate" required>
            </div>

            <div class="form-group">
                <label for="endDate">Thời gian kết thúc</label>
                <input type="datetime-local" id="endDate" name="endDate" required>
            </div>

            <div class="form-group">
                <button type="submit">Tạo đơn thuê</button>
                <button type="button" onclick="history.back()" class="action-btn" style="background-color: #6c757d; margin-left: 10px;">Quay lại</button>
            </div>
        </form>
    </div>

    <%@include file="footer.jsp"%>
</body>
</html>