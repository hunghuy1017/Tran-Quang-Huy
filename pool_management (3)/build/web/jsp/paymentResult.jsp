<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>KẾT QUẢ THANH TOÁN</title>
        <link href="${pageContext.request.contextPath}/vnpay_jsp/assets/bootstrap.min.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/vnpay_jsp/assets/jumbotron-narrow.css" rel="stylesheet"> 
        <script src="${pageContext.request.contextPath}/vnpay_jsp/assets/jquery-1.11.3.min.js"></script>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <h3 class="text-muted">KẾT QUẢ THANH TOÁN</h3>
            </div>
            <div class="table-responsive">
                <div class="form-group">
                    <label >Mã giao dịch thanh toán:</label>
                    <%-- Sử dụng EL để truy cập trực tiếp tham số từ request --%>
                    <label>${requestScope.vnp_TxnRef}</label> 
                </div>    
                <div class="form-group">
                    <label >Số tiền:</label>
                    <label>${requestScope.vnp_Amount / 100}</label> <%-- Chia lại cho 100 để hiển thị đúng tiền --%>
                </div>  
                <div class="form-group">
                    <label >Mô tả giao dịch:</label>
                    <label>${requestScope.vnp_OrderInfo}</label> 
                </div> 
                <div class="form-group">
                    <label >Mã lỗi thanh toán:</label>
                    <label>${requestScope.vnp_ResponseCode}</label> 
                </div> 
                <div class="form-group">
                    <label >Mã giao dịch tại CTT VNPAY-QR:</label>
                    <label>${requestScope.vnp_TransactionNo}</label> 
                </div> 
                <div class="form-group">
                    <label >Mã ngân hàng thanh toán:</label>
                    <label>${requestScope.vnp_BankCode}</label> 
                </div> 
                <div class="form-group">
                    <label >Thời gian thanh toán:</label>
                    <label>${requestScope.vnp_PayDate}</label> 
                </div> 
                <div class="form-group">
                    <label >Tình trạng giao dịch:</label>
                    <label>
                        <c:choose>
                            <c:when test="${requestScope.signatureValid}"> <%-- Kiểm tra chữ ký hợp lệ trước --%>
                                <c:choose>
                                    <c:when test="${requestScope.transResult}">
                                        Thành công
                                    </c:when>
                                    <c:otherwise>
                                        Không thành công
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                invalid signature
                            </c:otherwise>
                        </c:choose>
                    </label>
                </div> 
            </div>
            <a href="${pageContext.request.contextPath}/home" 
               style="display: inline-block; padding: 10px 20px; background-color: #3c6cc5; color: white; text-decoration: none; border-radius: 5px;">
                Quay về trang chủ
            </a>    
        </div>  
    </body>
</html>