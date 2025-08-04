<%-- 
    Document   : userHistoryPayment
    Created on : Jun 22, 2025, 12:29:13 PM
    Author     : LENOVO
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>History Page</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

        <style>
            /* Thiết lập font chữ và nền chung */
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f4f7f9;
                color: #333;
                margin: 0;
                padding: 20px;
            }

            /* Container chính của trang */
            .container {
                max-width: 1200px;
                margin: auto;
                background-color: #ffffff;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            }

            /* Phần header chứa tiêu đề và các nút hành động */
            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-bottom: 1px solid #e0e0e0;
                padding-bottom: 20px;
                margin-bottom: 20px;
            }

            .header h1 {
                margin: 0;
                font-size: 28px;
                color: #2c3e50;
            }

            .header .action-buttons a {
                text-decoration: none;
                color: #555;
                font-size: 24px;
                margin-left: 15px;
                transition: color 0.3s;
            }

            .header .action-buttons a:hover {
                color: #007bff;
            }

            /* Khu vực bộ lọc và tìm kiếm */
            .filters {
                display: flex;
                flex-wrap: wrap;
                gap: 20px;
                margin-bottom: 30px;
                align-items: flex-end;
            }

            .filter-group {
                display: flex;
                flex-direction: column;
            }

            .filter-group label {
                font-weight: 600;
                margin-bottom: 8px;
                font-size: 14px;
            }

            .filter-group input,
            .filter-group select {
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 14px;
                background-color: #fff;
            }

            .filter-group input[type="search"] {
                min-width: 250px;
            }

            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 14px;
                font-weight: bold;
                transition: background-color 0.3s;
            }

            .btn-primary {
                background-color: #007bff;
                color: white;
            }

            .btn-primary:hover {
                background-color: #0056b3;
            }

            /* Bảng hiển thị lịch sử giao dịch */
            .transaction-table table {
                width: 100%;
                border-collapse: collapse;
            }

            .transaction-table th,
            .transaction-table td {
                padding: 15px;
                text-align: left;
                border-bottom: 1px solid #e0e0e0;
            }

            .transaction-table th {
                background-color: #f8f9fa;
                font-size: 14px;
                text-transform: uppercase;
                color: #666;
            }

            .transaction-table tbody tr:hover {
                background-color: #f1f8ff;
            }

            .amount {
                font-weight: bold;
            }
            
            
            .amount.income {
                color: #5cb85c; /* Màu xanh cho thu nhập/hoàn tiền */
            }

            /* Trạng thái giao dịch */
            .status {
                padding: 5px 12px;
                border-radius: 15px;
                font-size: 12px;
                font-weight: bold;
                text-align: center;
                display: inline-block;
            }

            .status-success {
                background-color: #dff0d8;
                color: #3c763d;
            }

            .status-failed {
                background-color: #f2dede;
                color: #a94442;
            }

            .status-pending {
                background-color: #fcf8e3;
                color: #8a6d3b;
            }

            /* Phân trang */
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                margin-top: 30px;
            }

            .pagination a {
                color: #007bff;
                padding: 10px 15px;
                text-decoration: none;
                border: 1px solid #ddd;
                margin: 0 4px;
                border-radius: 6px;
                transition: background-color 0.3s;
            }

            .pagination a.active {
                background-color: #007bff;
                color: white;
                border-color: #007bff;
            }

            .pagination a:hover:not(.active) {
                background-color: #f0f0f0;
            }

        </style>
    </head>
    <body>

        <div class="container">
            <header class="header">
                <h1>Lịch Sử Giao Dịch</h1>
                <div class="action-buttons">
                    <a href="userDashboard  " title="Quay lại Dashboard"><i class="fa-solid fa-arrow-left"></i></a>
                </div>
            </header>

          <form action="userHistoryPayment" method="post">
    <section class="filters">
        <div class="filter-group">
            <label for="start-date">Từ ngày</label>
            <input type="date" id="start-date" name="start_date" value="${start_date}">
        </div>
        <div class="filter-group">
            <label for="end-date">Đến ngày</label>
            <input type="date" id="end-date" name="end_date" value="${end_date}">
        </div>
        <div class="filter-group">
            <label for="search">Tìm kiếm</label>
<input type="search" id="search" name="search" placeholder="Nhập tên gói..." value="${search}">
        </div>
        <button type="submit" class="btn btn-primary">Áp dụng</button>
    </section>
</form>

            <main class="transaction-table">
                <table>
                    <thead>
                        <tr>
                            <th>PaymantID</th>
                            <th>Ngày Giao Dịch</th>
                            <th>Gói mua</th>
                            <th>Số Tiền</th>
                            <th>Trạng Thái</th>
                        </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="pay" items="${listHistoryPayment}" varStatus="loop">
    <tr>
        <td>${pay.paymentID}</td>
        <td>${pay.paymentTime}</td>
        <td>${listNamePack[loop.index]}</td>
        <td class="amount expense">${pay.total}</td>
        <td>
            <c:choose>
                <c:when test="${pay.status eq 'Completed'}">
                    <span class="status status-success">${pay.status}</span>
                </c:when>
                <c:when test="${pay.status eq 'Pending'}">
                    <span class="status status-pending">${pay.status}</span>
                </c:when>
                <c:otherwise>
                    <span class="status status-failed">${pay.status}</span>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</c:forEach>

                    </tbody>
                </table>
            </main>

            <!--        <nav class="pagination">
                        <a href="#">&laquo; Trước</a>
                        <a href="#" class="active">1</a>
                        <a href="#">2</a>
                        <a href="#">3</a>
                        <a href="#">Sau &raquo;</a>
                    </nav>-->

        </div>

    </body>
</html>
