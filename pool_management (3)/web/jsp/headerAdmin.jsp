<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!-- Font và Icon -->
<link rel="stylesheet" href="https://fonts.google.com/specimen/Roboto">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title><c:out value="${pageTitle != null ? pageTitle : 'Admin Dashboard'}"/></title>

        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <!-- Chart.js (chỉ dùng ở trang cần biểu đồ) -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

        <!-- Bootstrap-icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet"/>

        <style>

            .logo {
                display: flex;
                align-items: center;
            }

            .logo a {
                display: flex;
                align-items: center;
                text-decoration: none;
            }

            .logo-icon {
                width: 36px;
                height: auto;
                margin-right: 8px;
            }

            .logo-text {
                font-size: 26px;
                font-weight: bold;
                color: #0077b6;
                font-family: 'Georgia', serif;
            }
            body            {
                background:#f6f7fb;
                font-family:"Segoe UI",Tahoma,Arial,sans-serif;
            }
            .rounded-box    {
                background:#fff;
                border:1px solid #d6dbe2;
                border-radius:18px;
                padding:32px;
            }
            .dashboard-nav  {
                background:#fff;
                border-bottom:1px solid #d6dbe2;
            }
            .dashboard-nav a{
                color:#333;
                font-weight:500;
                margin-right:24px;
                text-decoration:none;
                white-space:nowrap;
            }
            .dashboard-nav a:hover{
                text-decoration:underline;
            }
            .section-title  {
                font-size:1.1rem;
                font-weight:600;
                margin-bottom:12px;
            }
            .table thead    {
                background:#f0f2f7;
            }
            .table thead th {
                font-weight:600;
            }
            .card-link      {
                font-size:0.875rem;
            }
            /* --- CSS cho Header Actions --- */

.header-actions {
    display: flex; /* Sử dụng flexbox để các phần tử nằm ngang */
    align-items: center; /* Căn giữa theo chiều dọc */
    gap: 15px; /* Khoảng cách giữa các phần tử */
}

/* Kiểu dáng chung cho các nút/liên kết dạng icon */
.icon-btn {
    display: flex; /* Cho phép căn giữa icon và badge */
    align-items: center;
    justify-content: center;
    position: relative; /* Để định vị notification-badge */
    width: 35px; /* Kích thước cố định cho nút */
    height: 35px;
    border-radius: 50%; /* Làm tròn cho nút icon */
    background-color: transparent; /* Nền trong suốt */
    color: #fff; /* Màu icon trắng (nếu header nền tối) */
    font-size: 1.2em;
    text-decoration: none;
    transition: background-color 0.3s ease;
}

.icon-btn:hover {
    background-color: rgba(255, 255, 255, 0.1); /* Hiệu ứng hover nhẹ */
}

/* Badge thông báo */
.notification-badge {
    position: absolute;
    top: -5px;
    right: -5px;
    background-color: #dc3545; /* Màu đỏ nổi bật */
    color: white;
    font-size: 0.7em;
    padding: 2px 6px;
    border-radius: 50%;
    min-width: 18px;
    text-align: center;
    line-height: 1;
    border: 1px solid #333; /* Viền để nổi bật trên nền header */
}

/* Kiểu dáng chung cho các liên kết tài khoản */
.header_account {
    display: flex;
    align-items: center;
    gap: 8px; /* Khoảng cách giữa icon và chữ */
    color: #fff; /* Màu chữ trắng */
    text-decoration: none;
    font-weight: bold;
    padding: 5px 10px;
    border-radius: 5px;
    transition: background-color 0.3s ease;
    white-space: nowrap; /* Giữ các phần tử trên một dòng */
}

.header_account:hover {
    background-color: rgba(255, 255, 255, 0.1);
}

/* Icon bên trong các liên kết tài khoản */
.header_account .fa,
.icon-btn .fas {
    font-size: 1em; /* Kích thước icon */
}

/* Tên tài khoản */
#accountname {
    margin: 0; /* Bỏ margin mặc định của p */
    font-weight: bold;
}

/* Liên kết đăng nhập/đăng ký */
.login-link,
.register-btn {
    color: #fff;
    text-decoration: none;
    padding: 8px 12px;
    border-radius: 5px;
    transition: background-color 0.3s ease;
}

.login-link {
    background-color: #007bff; /* Nền xanh cho đăng nhập */
}
.login-link:hover {
    background-color: #0056b3;
}

.register-btn {
    background-color: #28a745; /* Nền xanh lá cho đăng ký */
}
.register-btn:hover {
    background-color: #218838;
}

/* Thông tin người dùng (Xin chào, vai trò) */
.user-info {
    display: flex;
    align-items: center;
    gap: 5px;
    color: #fff;
    white-space: nowrap;
}

.user-role {
    font-style: italic;
    color: #ced4da; /* Màu xám nhạt hơn cho vai trò */
}

/* Điều chỉnh nếu cần cho icon user profile */
.user-info .fa-user {
    font-size: 1.1em;
}

/* Ẩn phần notification-badge nếu không có thông báo */
/* Bạn sẽ cần JS để thêm/bỏ class hoặc chỉnh style display */
/* .notification-badge.hidden {
    display: none;
} */
        </style>
    </head>
    <body>

        <!-- ===== Thanh điều hướng ===== -->
        <nav class="dashboard-nav d-flex align-items-center px-4 py-2 shadow-sm">
            <div class="logo">
                <img src="${pageContext.request.contextPath}/images/swimming-logo.jpg" alt="Logo" class="logo-icon" />
                </a>
            </div>
            <div class="flex-grow-1 overflow-auto">
                <a href="SwimmingPoolManageServlet" class="dashboard-btn">Quản lý hồ bơi</a>
                <a href="#">Quản lý người dùng</a>
                <a href="${pageContext.request.contextPath}/ServletWarehouseManager">Quản lý kho</a>
                <a href="${pageContext.request.contextPath}/ServletRevenueReport">Quản lý doanh thu</a>
                <a href="#">Quản lý nhân viên</a>
                <a href="#">Quản lý HLV</a>
                <a href="#">Quản lý vai trò</a>
                <a href="${pageContext.request.contextPath}/Event">Quản lý sự kiện</a>
            </div>
            <div class="header-actions">
            <!-- Nếu chưa đăng nhập -->
            <c:if test="${sessionScope.user==null}">
                <a href="ServletUsers?service=loginUser" class="icon-btn notification-icon">
                    <i class="fas fa-bell"></i>
                    <span class="notification-badge">1</span>
                </a>
                <a class="header_account" href="home">
                    <i class="fa fa-user"></i>
                    <a href="${pageContext.request.contextPath}/jsp/LoginUser.jsp" class="login-link">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/jsp/insertUsers.jsp" class="register-btn">Đăng ký</a>
                </a>
            </c:if>

            <!-- Nếu đã đăng nhập -->
            <c:if test="${sessionScope.user!=null}">
                <a href="ServletNotification" class="icon-btn notification-icon">
                    <i class="fas fa-bell"></i>
                    <span class="notification-badge">1</span>
                </a>
            </c:if>

            <!-- Admin -->
            <c:if test="${sessionScope.user!=null && sessionScope.user.roleID==1}">
                <a class="header_account" href="AdminDashboardServlet">
                    <i class="fa fa-user"></i>
                    <p id="accountname">${user.fullName}</p>
                </a>
                <a class="header_account" onclick="location.href = 'ServletUsers?service=logout'">
                    <i class="fa fa-sign-out-alt"></i>
                </a>
            </c:if>

            <!-- Nhân viên -->
            <c:if test="${sessionScope.user!=null && sessionScope.user.roleID==3}">
                <a class="header_account" href="${pageContext.request.contextPath}/EmployeeProfile">
                    <i class="fa fa-user"></i>
                    <p id="accountname">${user.fullName}</p>
                </a>
                <a class="header_account" onclick="location.href = '${pageContext.request.contextPath}/ServletUsers?service=logout'">
                    <i class="fa fa-sign-out-alt"></i>
                </a>
            </c:if>

            <!-- Người dùng thường -->
            <c:if test="${sessionScope.user!=null && sessionScope.user.roleID!=1 && sessionScope.user.roleID!=3}">
                <a class="header_account" href="UserProfile">
                    <%
                        String roleName = (String) session.getAttribute("roleName");
                        String fullName = (session.getAttribute("user") != null) 
                                          ? ((models.Users) session.getAttribute("user")).getFullName()
                                          : null;
                    %>
                    <% if (fullName != null) { %>
                    <div class="user-info">
                        <i class="fa fa-user"></i>
                        Xin chào, <strong><%= fullName %></strong>
                        <% if (roleName != null) { %>
                        (<span class="user-role"><%= roleName %></span>)
                        <% } %>
                    </div>
                    <% } %>
                </a>
                <a class="header_account" onclick="location.href = '${pageContext.request.contextPath}/ServletUsers?service=logout'">
                    <i class="fa fa-sign-out-alt"></i>
                </a>
            </c:if>
        </div>
</nav>
