<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý kho</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/WarehouseManager.css">
    </head>
    <body>
        <jsp:include page="headerAdmin.jsp" /> 
        <div class="container">
            <h1>QUẢN LÝ KHO</h1>
            <c:choose>
                <c:when test="${position == 'Warehouse Management'}">
                    <button class="search-btn"><a href="EmployeeDashboard">Quay lại</a></button>
                </c:when>
                <c:otherwise>
                    <button class="search-btn"><a href="jsp/adminDashboard.jsp">Quay lại</a></button>
                </c:otherwise>
            </c:choose>


            <form action="ServletWarehouseManager">
                <div class="top-bar">
                    <div>
                        <c:if test="${RoleID == 1}">
                            <label>Chọn bể bơi: </label>
                            <select name="poolID" onchange="this.form.submit()">
                                <option value="0">Tất cả</option>
                                <c:forEach var="p" items="${pools}">
                                    <option value="${p.poolID}" ${p.poolID == poolID ? 'selected' : ''}>${p.name}</option>
                                </c:forEach>
                            </select>
                        </c:if>
                    </div>
                </div>
            </form>

            <c:choose>
                <c:when test="${position == 'Warehouse Management'}">

                </c:when>
                <c:otherwise>
                    <div style="margin-bottom: 20px;">
                        <c:if test="${not empty managerName}">
                            &#x1F464; Nhân viên quản lý: <strong>${managerName}</strong>
                        </c:if>
                    </div>
                </c:otherwise>
            </c:choose>


            <form action="ServletWarehouseManager">
                <input type="hidden" name="service" value="listProduct"/>

                <div class="top-bar">
                    <div class="search-group">
                        <input type="text" name="name" value="${name}" placeholder="Tìm kiếm theo tên" />
                        <button class="search-btn">Tìm kiếm</button>
                    </div>

                    <div class="view-group">
                        <label>Hiển thị:</label>
                        <select class="view-stock" name="status" onchange="this.form.submit()">
                            <option value="all" ${status == 'all' ? 'selected' : ''}>Tất cả</option>
                            <option value="for sale" ${status == 'for sale' ? 'selected' : ''}>Đang bán</option>
                            <option value="stop selling" ${status == 'stop selling' ? 'selected' : ''}>Ngừng bán</option>
                        </select>
                    </div>
                    <input type="hidden" name="poolID" value="${poolID}">
                    <div class="action-group">
                        <c:choose>
                            <c:when test="${position == 'Warehouse Management'}">

                            </c:when>
                            <c:otherwise>
                                <button class="btn-green" type="button">
                                    <a class="btn-green" href="ServletWarehouseManager?service=AddProduct">Thêm sản phẩm</a>
                                </button>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
            </form>

            <h3>&#x1F4E6; Danh sách sản phẩm trong kho</h3>
            <table>
                <tr>
                    <th>Tên sản phẩm</th>
                    <th>Hình ảnh</th>
                    <th>Mô tả</th>
                    <th>Số lượng</th>
                    <th>Giá</th>
                    <th>Loại</th>
                    <th>Cho thuê</th>
                    <th>Giá thuê</th>
                    <th>Ngày thêm</th>
                    <th>Trạng thái</th>
                    <th></th>
                    <th></th>
                </tr>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>${p.productName}</td>
                        <td><img src="${pageContext.request.contextPath}/images/products/${p.image}" class="icon-img" alt="icon"/></td>
                        <td>${p.description}</td>
                        <td>${p.quantity}</td>
                        <td>${p.price} VNĐ</td>
                        <td>
                            <c:forEach var="c" items="${Categories}">
                                <c:if test="${p.categoryID == c.categoryID}">
                                    ${c.categoryName}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>${p.isRentable ? 'Có' : 'Không'}</td>
                        <td>${p.rentalPrice} VNĐ</td>
                        <td>${p.addedDate}</td>
                        <td class="${p.status ? 'status-green' : 'status-red'}">
                            ${p.status ? 'Đang bán' : 'Ngừng bán'}
                        </td>
                        <td class="actions"><a href="ServletWarehouseManager?service=UpdateProduct&pID=${p.productID}">&#x270F;</a>️</td>
                        <c:choose>
                            <c:when test="${position == 'Warehouse Management'}">

                            </c:when>
                            <c:otherwise>
                                <td class="actions"><a href="#" onclick="showMess(${p.productID})">&#x1F5D1;</a>️</td>
                            </c:otherwise>
                        </c:choose>
                        
                    </tr>
                </c:forEach>
                <c:if test="${empty products}">
                    <tr><td colspan="9" style="text-align:center;">Không tìm thấy sản phẩm nào.</td></tr>
                </c:if>
            </table>

            <div class="pagination">
                <c:if test="${page > 1}">
                    <a href="ServletWarehouseManager?service=listProduct&poolID=${poolID}&name=${name}&status=${status}&page=${page - 1}">
                        <button>&lt;</button>
                    </a>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="ServletWarehouseManager?service=listProduct&poolID=${poolID}&name=${name}&status=${status}&page=${i}">
                        <button ${i == page ? 'disabled' : ''}>${i}</button>
                    </a>
                </c:forEach>
                <c:if test="${page < totalPages}">
                    <a href="ServletWarehouseManager?service=listProduct&poolID=${poolID}&name=${name}&status=${status}&page=${page + 1}">
                        <button>&gt;</button>
                    </a>
                </c:if>
            </div>

            <div class="statistical">
                <h3>&#x1F4CA; Thống kê</h3>
                <p>&#x1F477; Tổng số nhân viên kho: <strong>${totalEmployees}</strong></p>
                <p>&#x1F4E6; Tổng số sản phẩm: <strong>${totalProducts}</strong></p>
            </div>
        </div>

        <div id="confirm-popup" class="confirm-popup hidden">
            <div class="confirm-box">
                <p>Bạn có chắc chắn muốn xóa sản phẩm này không?</p>
                <div class="buttons">
                    <button id="confirm-yes">Có</button>
                    <button id="confirm-no">Không</button>
                </div>
            </div>
        </div>
    </body>
    <script>
        let deleteId = null;

        function showMess(productID) {
            deleteId = productID;
            document.getElementById('confirm-popup').classList.remove('hidden');
        }

        document.getElementById('confirm-yes').addEventListener('click', function () {
            if (deleteId !== null) {
                window.location.href = 'ServletWarehouseManager?service=DeleteProduct&pID=' + deleteId;
            }
        });

        document.getElementById('confirm-no').addEventListener('click', function () {
            document.getElementById('confirm-popup').classList.add('hidden');
            deleteId = null;
        });
    </script>
</html>
