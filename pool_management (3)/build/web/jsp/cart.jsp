<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ Hàng</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/seller.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/Seller">Giỏ Hàng</a>
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
    <div class="container shopping-cart">
        <h2 class="text-center mb-4">Giỏ Hàng Của Bạn</h2>
        <c:if test="${not empty param.message}">
            <p class="message">${param.message}</p>
        </c:if>
        <div class="row">
            <div class="col-lg-12 p-4 bg-white rounded shadow-sm">
                <div class="table-responsive">
                    <table class="table">
                        <thead class="bg-light">
                            <tr>
                                <th>Sản Phẩm</th>
                                <th>Đơn Giá</th>
                                <th>Số Lượng</th>
                                <th>Tổng Cộng</th>
                                <th>Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${cartItems}" var="item">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <img src="${pageContext.request.contextPath}/images/products/${item. productImage}" alt="${item.productName}" width="70" class="img-fluid rounded shadow-sm mr-3">
                                            <h5 class="mb-0">${item.productName}</h5>
                                        </div>
                                    </td>
                                    <td><strong>${item.price} VND</strong></td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <a href="${pageContext.request.contextPath}/Seller?action=decrease&productID=${item.productID}" class="btn btn-outline-dark btn-sm mr-2">-</a>
                                            <strong>${item.quantity}</strong>
                                            <a href="${pageContext.request.contextPath}/Seller?action=increase&productID=${item.productID}" class="btn btn-outline-dark btn-sm ml-2">+</a>
                                        </div>
                                    </td>
                                    <td><strong>${item.price * item.quantity} VND</strong></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/Seller?action=removeFromCart&productID=${item.productID}" class="btn btn-danger btn-sm">Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="row mt-4">
            <div class="col-lg-6 offset-lg-6">
                <div class="bg-white rounded shadow-sm p-4">
                    <h5 class="text-uppercase mb-3">Tổng Thanh Toán</h5>
                    <ul class="list-unstyled">
                        <li class="d-flex justify-content-between py-2 border-bottom">
                            <strong>Tổng tiền hàng</strong>
                            <strong>${total} VND</strong>
                        </li>
                        <li class="d-flex justify-content-between py-2 border-bottom">
                            <strong>Số tiền khách trả</strong>
                            <input type="number" id="paidAmount" name="paidAmount" class="form-control paid-amount-input" min="0" step="1000" placeholder="Nhập số tiền">
                        </li>
                        <li id="changeRow" class="d-flex justify-content-between py-2 border-bottom" style="display: none;">
                            <strong>Tiền thừa</strong>
                            <strong id="changeAmount">0 VND</strong>
                        </li>
                    </ul>
                    <a href="#" id="checkoutButton" class="btn btn-dark btn-block mt-3">Xác Nhận Thanh Toán</a>
                </div>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script>
        document.getElementById('paidAmount').addEventListener('input', function() {
            const total = ${total};
            const paidAmount = parseFloat(this.value) || 0;
            const change = paidAmount - total;
            const changeRow = document.getElementById('changeRow');
            const changeAmount = document.getElementById('changeAmount');
            if (paidAmount > 0) {
                changeRow.style.display = 'flex';
                changeAmount.textContent = change >= 0 ? change.toFixed(2) + ' VND' : 'Số tiền không đủ';
            } else {
                changeRow.style.display = 'none';
            }
        });

        document.getElementById('checkoutButton').addEventListener('click', function(e) {
            e.preventDefault();
            const paidAmount = document.getElementById('paidAmount').value;
            const total = ${total};
            if (!paidAmount || parseFloat(paidAmount) < total) {
                alert('Vui lòng nhập số tiền khách trả đủ hoặc lớn hơn tổng tiền hàng.');
                return;
            }
            window.location.href = "${pageContext.request.contextPath}/Seller?action=checkout&paidAmount=" + encodeURIComponent(paidAmount);
        });
    </script>
</body>
</html>