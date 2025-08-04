<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác Nhận Đơn Hàng</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/seller.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/Seller">Xác Nhận Đơn Hàng</a>
            <div class="header-links">
                <a href="${pageContext.request.contextPath}/Seller" class="nav-link">Quay lại</a>
                <a href="${pageContext.request.contextPath}/Seller?action=viewCart" class="nav-link cart-icon">🛒
                    <c:if test="${cartItemCount > 0}">
                        <span class="cart-count">${cartItemCount}</span>
                    </c:if>
                </a>
            </div>
        </div>
    </nav>
    <div class="container mt-5">
        <div class="row">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h3 class="mb-0">Xác Nhận Đơn Hàng</h3>
                    </div>
                    <div class="card-body">
                        <h4 class="card-title">Cảm ơn bạn đã mua hàng!</h4>
                        <div class="row">
                            <div class="col-md-6">
                                <h5>Thông Tin Đơn Hàng</h5>
                                <p><strong>Mã đơn hàng:</strong> ${order.orderID}</p>
                                <p><strong>Ngày mua hàng:</strong> ${order.orderDate}</p>
                                <p><strong>Tổng tiền:</strong> ${order.total} VND</p>
                                <p><strong>Số tiền khách trả:</strong> ${paidAmount} VND</p>
                                <p><strong>Tiền thừa:</strong> ${change} VND</p>
                                <p><strong>Mã người dùng:</strong> ${order.userID}</p>
                            </div>
                            <div class="col-md-6">
                                <h5>Sản Phẩm Đã Mua</h5>
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Sản phẩm</th>
                                            <th>Đơn giá</th>
                                            <th>Số lượng</th>
                                            <th>Tổng cộng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${cartItems}" var="item">
                                            <tr>
                                                <td>${item.productName}</td>
                                                <td>${item.price} VND</td>
                                                <td>${item.quantity}</td>
                                                <td>${item.price * item.quantity} VND</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="mt-4">
                            <a href="${pageContext.request.contextPath}/Seller" class="btn btn-primary">Tiếp Tục Bán Hàng</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>