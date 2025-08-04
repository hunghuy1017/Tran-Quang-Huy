<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Font và Icon -->
<link rel="stylesheet" href="https://fonts.google.com/specimen/Roboto">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">

<!-- CSS cho header -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />

<header class="main-header">
    <div class="header-container">
        <!-- Logo -->
        <div class="logo">
            <a href="${pageContext.request.contextPath}/home">
                <img src="${pageContext.request.contextPath}/images/swimming-logo.jpg" alt="Logo" class="logo-icon" />
                <span class="logo-text">BluePool</span>
            </a>
        </div>

        <!-- Menu -->
        <nav class="nav-menu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                <li><a href="jsp/blog.jsp">Blog</a></li>
                <li><a href="<%= request.getContextPath() %>/trainer-list">Đặt huấn luyện viên</a>
                <li><a href="${pageContext.request.contextPath}/Package?service=listPackages">Gói bơi</a></li>
                <li><a href="${pageContext.request.contextPath}/Event">Sự kiện</a></li>
                
                <li><a href="jsp/about.jsp">Giới thiệu</a></li>
            </ul>
        </nav>

        <!-- Hành động bên phải -->
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
                <a class="header_account" href="EmployeeDashboard">
                    <i class="fa fa-user"></i>
                    <p id="accountname">${user.fullName}</p>
                </a>
                <a class="header_account" onclick="location.href = 'ServletUsers?service=logout'">
                    <i class="fa fa-sign-out-alt"></i>
                </a>
            </c:if>

            <!-- Người dùng thường -->
            <c:if test="${sessionScope.user!=null && sessionScope.user.roleID!=1 && sessionScope.user.roleID!=3}">
                <a class="header_account" href="userDashboard">
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
                <a class="header_account" onclick="location.href = 'ServletUsers?service=logout'">
                    <i class="fa fa-sign-out-alt"></i>
                </a>
            </c:if>
        </div>
    </div>
</header>
