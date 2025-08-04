<%-- userPayment.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Payment Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userPayment.css">
    </head>
    <%@include file="../jsp/header.jsp" %>
    <body>
        <form id="paymentForm" action="${pageContext.request.contextPath}/userPayment" method="POST">
            <div class="container">
                <h1>Xác Nhận & Thanh Toán Gói Bơi</h1>
                <div class="payment-content">
                    <div class="order-summary">
                        <h2>Thông Tin Gói Đặt Mua</h2>
                        <div class="order-item">
                            <p class="package-name">${requestScope.selectedPackageName}</p>
                            <p>Mô tả: Valid for ${requestScope.selectedPackageDuration} days from the activation date</p>
                            <p>Thời hạn: ${requestScope.selectedPackageDuration} ngày</p>
                            <p>Giá: 
                                <span>
                                    <fmt:formatNumber value="${selectedPackagePrice+0}" type="currency" currencySymbol="" maxFractionDigits="0" />
                                </span>
                            </p>
                        </div>
                        <div class="total-amount">
                            Tổng cộng:
                            <span id="totalAmount">
                                <fmt:formatNumber  value="${selectedPackagePrice+0}" type="currency" currencySymbol="" maxFractionDigits="0" />
                            </span>
                        </div>
                    </div>
                    <div class="payment-form">
                        <h2>Thông Tin Thanh Toán</h2>
                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email" value="${requestScope.userEmail}" required>
                        </div>
                        <div class="form-group">
                            <label>Chọn Phương Thức Thanh Toán:</label>
                            <div class="radio-group">
                                <label><input type="radio" name="paymentMethod" value="vnpay" checked> VNPay</label>
                                
                            </div>
                        </div>
                        <input type="hidden" name="packageId" id="packageIdInput" value="${requestScope.selectedPackageId}" />
                        <input type="hidden" name="total" id="totalInput" value="${requestScope.selectedPackagePrice}" />
                        <button type="submit" class="btn btn-pay">Xác nhận thanh toán</button>
                    </div>
                </div>
            </div>
        </form>
    </body>
    <%@include file="../jsp/footer.jsp" %>
</html>