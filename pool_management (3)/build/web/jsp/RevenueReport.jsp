<!DOCTYPE html>
<html lang="vi">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <head>
        <meta charset="UTF-8">
        <title>Báo cáo doanh thu</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/PaymentManager.css">
    </head>
    <body>
        <jsp:include page="headerAdmin.jsp" /> 
        <div class="container">
            <div class="header">
                <h1>Quản lý doanh thu</h1>
            </div>

            <button class="btn-back"><a href="jsp/adminDashboard.jsp">Quay lại</a></button>

            <div class="filter-section">
                <div class="filter-left">
                    <div class="filter-group"></div>
                    <div class="filter-group"></div>
                </div>

                <div class="button-group">
                    <button class="btn-blue" type="submit">Xem biểu đồ doanh thu</button>
                    <button class="btn-blue">Phân tích</button>
                </div>
            </div>
        </form>

        <section>
            <h2>Tổng doanh thu</h2>
            <table>
                <thead>
                    <tr>
                        <th>Tổng</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="highlight">${total} VNĐ</td>
                    </tr>
                </tbody>
            </table>
        </section>

        <c:if test="${RoleID == 1}">
            <section>
                <h2>Doanh thu theo bể bơi</h2>
                <form action="ServletRevenueReport" class="search-form">
                    <div class="search-group">
                        <input type="text" name="PoolName" value="${PoolName}" placeholder="Tìm theo tên bể bơi" class="search-input">
                        <span class="search-icon"></span>
                    </div>
                    <button type="submit" class="search-button">Tìm kiếm</button>
                </form>
                <table id="pool-table">
                    <thead>
                        <tr>
                            <th>Bể bơi</th>
                            <th>Doanh thu</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="SP" items="${listSP}">
                            <tr>
                                <td>${SP.swimmingpool}</td>
                                <td>${SP.revenue} VNĐ</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div id="pool-pagination" class="pagination"></div>
            </section>
        </c:if>

        <section>
            <h2>Doanh thu theo sản phẩm</h2>
            <form action="ServletRevenueReport" method="get" class="search-form">
                <div class="search-group">
                    <input type="text" name="productName" value="${productName}" placeholder="Tìm theo tên sản phẩm" class="search-input">
                    <span class="search-icon"></span>
                </div>

                <div class="search-group">
                    <label class="search-label">Trạng thái</label>
                    <select name="Status">
                        <option value="all" ${Status == 'all' ? 'selected' : ''}>Tất cả</option>
                        <option value="true" ${Status == 'true' ? 'selected' : ''}>Bán</option>
                        <option value="false" ${Status == 'false' ? 'selected' : ''}>Cho thuê</option>
                    </select>
                </div>

                <div class="search-group">
                    <label class="search-label">Ngày đặt</label>
                    <input type="date" name="OrderDate" value="${OrderDate}" class="search-date">
                </div>

                <button type="submit" class="search-button">Tìm kiếm</button>
            </form>
            <table id="product-table">
                <thead>
                    <tr>
                        <th>Tên người dùng</th>
                        <th>Tên sản phẩm</th>
                        <th>Số lượng</th>
                        <th>Trạng thái</th>
                        <th>Tổng tiền</th>
                        <th>Ngày đặt</th>
                        <th>Bể bơi</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${listP}">
                        <tr>
                            <td>${p.userName}</td>
                            <td>${p.productName}</td>
                            <td>${p.quantity}</td>
                            <td>
                                <c:if test="${p.status == true}">Bán</c:if>
                                <c:if test="${p.status == false}">Cho thuê</c:if>
                            </td>
                            <td>${p.total} VNĐ</td>
                            <td>${p.orderDate}</td>
                            <td>${p.swimmingpool}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div id="product-pagination" class="pagination"></div>
        </section>

        <section>
            <h2>Doanh thu theo gói</h2>
            <form action="ServletRevenueReport" method="get" class="search-form">
                <div class="search-group">
                    <input type="text" name="Packagename" value="${Packagename}" placeholder="Tìm theo tên gói" class="search-input">
                    <span class="search-icon"></span>
                </div>

                <div class="search-group">
                    <label class="search-label">Thời gian thanh toán</label>
                    <input type="date" name="paymentTime" value="${paymentTime}" class="search-date">
                </div>

                <button type="submit" class="search-button">Tìm kiếm</button>
            </form>
            <table id="package-table">
                <thead>
                    <tr>
                        <th>Tên người dùng</th>
                        <th>Tên gói</th>
                        <th>Bể bơi</th>
                        <th>Phương thức thanh toán</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th>Thời gian thanh toán</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pk" items="${listPK}">
                        <tr>
                            <td>${pk.userName}</td>
                            <td>${pk.packageName}</td>
                            <td>${pk.swimmingpool}</td>
                            <td>${pk.paymentMethod}</td>
                            <td>${pk.total} VNĐ</td>
                            <td>${pk.status}</td>
                            <td>${pk.paymentTime}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div id="package-pagination" class="pagination"></div>
        </section>

        <section>
            <h2>Doanh thu theo lịch huấn luyện viên</h2>
            <form action="ServletRevenueReport" method="get" class="search-form">
                <div class="search-group">
                    <input type="text" name="Username" value="${Username}" placeholder="Tìm theo người dùng" class="search-input">
                    <span class="search-icon"></span>
                </div>
                <div class="search-group">
                    <input type="text" name="Trainername" value="${Trainername}" placeholder="Tìm theo huấn luyện viên" class="search-input">
                    <span class="search-icon"></span>
                </div>

                <div class="search-group">
                    <label class="search-label">Ngày thanh toán</label>
                    <input type="date" name="PaymentDate" value="${PaymentDate}" class="search-date">
                </div>

                <button type="submit" class="search-button">Tìm kiếm</button>
            </form>
            <table id="trainer-booking-table">
                <thead>
                    <tr>
                        <th>Tên người dùng</th>
                        <th>Tên huấn luyện viên</th>
                        <th>Bể bơi</th>
                        <th>Tên lớp</th>
                        <th>Phương thức thanh toán</th>
                        <th>Tổng tiền</th>
                        <th>Ngày thanh toán</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="Rb" items="${listRB}">
                        <tr>
                            <td>${Rb.userName}</td>
                            <td>${Rb.trainerName}</td>
                            <td>${Rb.swimmingPool}</td>
                            <td>${Rb.className}</td>
                            <td>${Rb.paymentMethod}</td>
                            <td>${Rb.total} VNĐ</td>
                            <td>${Rb.paymentDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div id="trainer-booking-pagination" class="pagination"></div>
        </section>
    </div>
</body>

<script>
    function paginateTable(tableId, paginationId, rowsPerPage) {
        const table = document.getElementById(tableId);
        const tbody = table.querySelector("tbody");
        const rows = Array.from(tbody.querySelectorAll("tr"));
        const pagination = document.getElementById(paginationId);
        const totalPages = Math.ceil(rows.length / rowsPerPage);
        let currentPage = 1;

        function showPage(page) {
            const start = (page - 1) * rowsPerPage;
            const end = page * rowsPerPage;

            rows.forEach((row, index) => {
                row.style.display = (index >= start && index < end) ? "" : "none";
            });

            renderPagination();
        }

        function renderPagination() {
            pagination.innerHTML = "";
            for (let i = 1; i <= totalPages; i++) {
                const btn = document.createElement("button");
                btn.innerText = i;
                btn.className = (i === currentPage) ? "active-page" : "";
                btn.onclick = function () {
                    currentPage = i;
                    showPage(currentPage);
                };
                pagination.appendChild(btn);
            }
        }

        showPage(currentPage);
    }

    window.onload = function () {
        paginateTable("product-table", "product-pagination", 10);
        paginateTable("pool-table", "pool-pagination", 10);
        paginateTable("package-table", "package-pagination", 10);
        paginateTable("trainer-booking-table", "trainer-booking-pagination", 10);
    };
</script>
</html>
