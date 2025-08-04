<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý thuê đồ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/rentalManager.css">
</head>
<body>
    <%@include file="header.jsp"%>

    <div class="container">
        <h2>Quản lý thuê đồ</h2>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <div class="search-bar">
            <form action="RentalManager" method="get">
                <input type="text" name="search" value="${searchTerm}" placeholder="Tìm theo tên khách hàng hoặc số điện thoại">
                <button type="submit">Tìm kiếm</button>
            </form>
            <a href="RentalManager?action=createRental" class="action-btn create-btn">Tạo đơn thuê mới</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Tên khách hàng</th>
                    <th>Số điện thoại</th>
                    <th>Sản phẩm</th>
                    <th>Thời gian bắt đầu</th>
                    <th>Thời gian kết thúc</th>
                    <th>Giá thuê/giờ</th>
                    <th>Tổng chi phí</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="rental" items="${rentals}">
                    <tr>
                        <td>${rental.customerName}</td>
                        <td>${rental.phoneNumber}</td>
                        <td>${rental.productName}</td>
                        <td><fmt:formatDate value="${rental.startDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td><fmt:formatDate value="${rental.endDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td><fmt:formatNumber value="${rental.rentalPrice}" type="currency" currencySymbol="VND"/></td>
                        <td><fmt:formatNumber value="${rental.totalCost}" type="currency" currencySymbol="VND"/></td>
                        <td>${rental.status}</td>
                        <td>
                            <c:if test="${rental.status == 'Active'}">
                                <a href="RentalManager?action=completeRental&rentalID=${rental.rentalID}" class="action-btn complete-btn">Hoàn thành</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty rentals}">
                    <tr>
                        <td colspan="9">Không có đơn thuê nào.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <div class="form-group">
            <button type="button" onclick="history.back()" class="action-btn" style="background-color: #6c757d;">Quay lại</button>
        </div>
    </div>

    <%@include file="footer.jsp"%>
</body>
</html>