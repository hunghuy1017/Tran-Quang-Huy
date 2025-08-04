<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Dashboard</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/seller.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/EmployeeDashboard">Seller Dashboard</a>
            <div class="header-links">
                <a href="${pageContext.request.contextPath}/EmployeeDashboard" class="nav-link">Quay lại</a>
                <a href="${pageContext.request.contextPath}/Seller?action=viewCart" class="nav-link cart-icon">🛒
                    <c:if test="${cartItemCount > 0}">
                        <span class="cart-count">${cartItemCount}</span>
                    </c:if>
                </a>
            </div>
        </div>
    </nav>
    <div class="container">
        <div class="filters">
            <div class="category-filter">
                <label for="categoryID">Danh mục:</label>
                <select name="categoryID" id="categoryID" onchange="filterProducts()">
                    <option value="0">Tất cả danh mục</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.categoryID}" ${categoryID == category.categoryID ? 'selected' : ''}>${category.categoryName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="search-bar">
                <form id="searchForm">
                    <input type="text" name="search" value="${searchTerm}" placeholder="Tìm sản phẩm..." id="searchInput">
                    <button type="button" onclick="searchProducts()">Tìm</button>
                </form>
            </div>
        </div>
        <div class="product-section">
            <h2>Danh sách sản phẩm</h2>
            <c:if test="${not empty error}">
                <p class="message">${error}</p>
            </c:if>
            <c:if test="${not empty param.message}">
                <p class="message">${param.message}</p>
            </c:if>
            <div class="product-grid" id="productList">
                <c:choose>
                    <c:when test="${not empty products}">
                        <c:forEach var="product" items="${products}">
                            <div class="product-card">
                                <img src="${pageContext.request.contextPath}/images/products/${product.image}" alt="${product.productName}" onerror="this.src='${pageContext.request.contextPath}/images/default-product.jpg';">
                                <h3>${product.productName}</h3>
                                <p class="price">${product.price} VND</p>
                                <form method="post" action="${pageContext.request.contextPath}/Seller">
                                    <input type="hidden" name="action" value="addToCart">
                                    <input type="hidden" name="productID" value="${product.productID}">
                                    <input type="hidden" name="search" value="${searchTerm}">
                                    <input type="hidden" name="page" value="${currentPage}">
                                    <input type="hidden" name="categoryID" value="${categoryID}">
                                    <div class="quantity-control">
                                        <button type="button" onclick="decreaseQuantity(${product.productID})">-</button>
                                        <input type="number" id="quantity-${product.productID}" name="quantity" value="1" min="1" max="${product.quantity}" readonly>
                                        <button type="button" onclick="increaseQuantity(${product.productID}, ${product.quantity})">+</button>
                                    </div>
                                    <button type="submit">Thêm vào giỏ</button>
                                </form>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>Không có sản phẩm nào để hiển thị.</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/Seller?search=${searchTerm}&page=${currentPage - 1}&categoryID=${categoryID}" class="pagination-btn">Trước</a>
                </c:if>
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <a href="${pageContext.request.contextPath}/Seller?search=${searchTerm}&page=${i}&categoryID=${categoryID}" class="${i == currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/Seller?search=${searchTerm}&page=${currentPage + 1}&categoryID=${categoryID}" class="pagination-btn">Tiếp</a>
                </c:if>
            </div>
        </div>
    </div>
    <script>
        function searchProducts() {
            const searchTerm = document.getElementById('searchInput').value;
            const categoryID = document.getElementById('categoryID').value;
            window.location.href = "${pageContext.request.contextPath}/Seller?search=" + encodeURIComponent(searchTerm) + "&categoryID=" + categoryID + "&page=1";
        }

        function filterProducts() {
            const categoryID = document.getElementById('categoryID').value;
            window.location.href = "${pageContext.request.contextPath}/Seller?search=${searchTerm}&categoryID=" + categoryID + "&page=1";
        }

        function increaseQuantity(productID, maxQuantity) {
            const input = document.getElementById('quantity-' + productID);
            let value = parseInt(input.value);
            if (value < maxQuantity) {
                input.value = value + 1;
            }
        }

        function decreaseQuantity(productID) {
            const input = document.getElementById('quantity-' + productID);
            let value = parseInt(input.value);
            if (value > 1) {
                input.value = value - 1;
            }
        }
    </script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>