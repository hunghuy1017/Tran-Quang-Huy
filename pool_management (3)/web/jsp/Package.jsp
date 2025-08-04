<%--
    Document   : Package.jsp
    Created on : May 29, 2025, 7:43:48 AM
    Author     : LENOVO
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang Gói Dịch Vụ</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Package.css">
    </head>
    <body>
        <%@include file="../jsp/header.jsp" %>

        <div style="text-align: center; margin-top: 30px;">
            <h1>Danh sách các gói bơi</h1>
        </div>

        <div class="notifications-container">
            <div class="header-row">
                <form action="Package" method="get" class="sort-form">
                    <input type="hidden" name="service" value="listPackages">
                    <input type="hidden" name="searchQuery" value="${param.searchQuery}"> <%-- Giữ lại tham số tìm kiếm khi sắp xếp --%>
                    <input type="hidden" name="eventType" value="${param.eventType}"> <%-- Giữ lại tham số loại sự kiện khi sắp xếp --%>
                    <div class="header-left">
                        <h2>Các Gói</h2>
                        <select name="sort" onchange="this.form.submit()">
                            <option value="desc" ${param.sort == 'desc' ? 'selected' : ''}>Giá Cao-Thấp</option>
                            <option value="asc" ${param.sort == 'asc' ? 'selected' : ''}>Giá Thấp-Cao</option>
                        </select>
                        <!--        <%-- Nút hiện tất cả goi --%>
                            <button type="submit" name="eventType" value="all" 
                                    class="event-button ${param.eventType == 'all' || param.eventType == null ? 'active' : ''}">Tất cả Gói</button>
                        -->

                        <%-- Select box cho địa điểm bể bơi --%>
                        <select name="poolId" onchange="this.form.submit()" class="pool-select">
                            <option value="">Chọn Địa Điểm</option>
                            <c:forEach var="pool" items="${allPools}">
                                <option value="${pool.poolID}" ${selectedPoolId == pool.poolID ? 'selected' : ''}>
                                    ${pool.name} - ${pool.location}
                                </option>
                            </c:forEach>
                        </select>

                    </div>
                </form>

                <form action="Package" method="get" class="search-form">
                    <input type="hidden" name="service" value="listPackages">
                    <input type="hidden" name="sort" value="${param.sort}"> <%-- Giữ lại tham số sắp xếp khi tìm kiếm --%>
                    <input type="hidden" name="eventType" value="${param.eventType}"> <%-- Giữ lại tham số loại sự kiện khi tìm kiếm --%>
                    <div class="filters">
                        <input type="text" name="searchQuery" value="${param.searchQuery}" placeholder="Tìm kiếm...">
                        <button type="submit">Tìm kiếm</button>
                    </div>
                </form>
            </div>

            <c:if test="${not empty mess}">
                <p style="color: #00cc33">${mess}</p>
            </c:if>

            <div class="container">
                <c:choose>
                    <c:when test="${not empty packagesDisplay}">
                        <c:forEach var="p" items="${packagesDisplay}">
                            <div class="package">
                                <h2>${p.packageName}</h2>
                                <p>${p.description}</p>
                                <%-- Chỉ hiển thị Duration Days nếu có giá trị khác 0 --%>
                                <c:if test="${p.durationInDays != null && p.durationInDays != 0}">
                                    <p><strong>Thời lượng:</strong> ${p.durationInDays} ngày</p>
                                </c:if>
                                <p class="price">${p.price} VND</p>
                                <c:if test="${sessionScope.user==null}">
                                    <a href="ServletUsers?service=loginUser" class="btn">Mua ngay</a>
                                </c:if>
                                <c:if test="${sessionScope.user!=null}">
                                    <a href="Package?service=buyPackage&packageId=${p.packageID}" class="btn">Mua ngay</a>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p style="text-align: center; font-size: 1.2em; color: gray;">Không có gói dịch vụ nào để hiển thị.</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- Phân trang --%>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="Package?service=listPackages&page=${currentPage - 1}&searchQuery=${param.searchQuery}&sort=${param.sort}&eventType=${param.eventType}">&lt;</a>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="Package?service=listPackages&page=${i}&searchQuery=${param.searchQuery}&sort=${param.sort}&eventType=${param.eventType}" 
                       class="${currentPage == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="Package?service=listPackages&page=${currentPage + 1}&searchQuery=${param.searchQuery}&sort=${param.sort}&eventType=${param.eventType}">&gt;</a>
                </c:if>
            </div>
        </div>
        <%@include file="../jsp/footer.jsp" %>
    </body>
</html>