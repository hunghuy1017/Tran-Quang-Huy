<!DOCTYPE html>
<html lang="vi">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <head>
        <meta charset="UTF-8" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/AddProduct.css">
        <title>Thêm Sản Phẩm</title>
    </head>
    <body>
        <div class="form-container">
            <h2>Thêm Sản Phẩm</h2>

            <div class="back-btn">
                <button><a href="ServletWarehouseManager">QUAY LẠI</a></button>
            </div>
            <form action="ServletWarehouseManager?service=AddProduct" method="post" enctype="multipart/form-data">
                <div class="form-row">
                    <div class="form-group">
                        <label>Bể Bơi</label>
                        <c:choose>
                            <c:when test="${position == 'Manager'}">
                                <select name="poolID" required>
                                    <c:forEach var="pool" items="${pools}">
                                        <c:if test="${pool.poolID == poolID}">
                                            <option value="${pool.poolID}">${pool.name}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </c:when>
                            <c:otherwise>
                                <select name="poolID" required>
                                    <c:forEach var="pool" items="${pools}">
                                        <option value="${pool.poolID}">${pool.name}</option>
                                    </c:forEach>
                                </select>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group">
                        <label>Tên Sản Phẩm</label>
                        <input type="text" name="productName" required />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Mô Tả</label>
                        <input type="text" name="description" />
                    </div>
                    <div class="form-group">
                        <label>Giá</label>
                        <input type="number"  step="0.01" name="price" required />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Ngày Nhập</label>
                        <input type="datetime-local" name="addedDate" required />
                    </div>
                    <div class="form-group">
                        <label>Số Lượng</label>
                        <input type="number" name="quantity" required />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Hình Ảnh</label>
                        <input type="file" name="image" accept="image/*" onchange="checkFileSize(this)" required />
                        <script>
                            function checkFileSize(input) {
                                const maxSize = 2 * 1024 * 1024; // 500KB in bytes
                                if (input.files.length > 0 && input.files[0].size > maxSize) {
                                    alert("File quá lớn! Vui lòng chọn file nhỏ hơn 2mbMB.");
                                    input.value = ""; // Xóa file đã chọn
                                }
                            }
                        </script>
                    </div>
                    <div class="form-group">
                        <label>Giá thuê</label>
                        <input type="number" step="0.01" name="RentalPrice" required />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Danh Mục</label>
                        <select name="Categories" required>
                            <c:forEach var="c" items="${Categories}">
                                <option value="${c.categoryID}">${c.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Cho thuê</label>
                        <select name="IsRentable" required>                         
                            <option value="true">Có</option>
                            <option value="false">Không</option>                           
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Trạng Thái</label>
                        <div class="status-group">
                            <div class="status-option green">
                                <input type="radio" id="forSale" name="status" value="true" checked />
                                <label for="forSale">Đang Bán</label>
                            </div>
                            <div class="status-option red">
                                <input type="radio" id="stopSelling" name="status" value="false" />
                                <label for="stopSelling">Ngừng Bán</label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">

                    </div>
                </div>

                <div class="submit-btn">
                    <button name="submit" type="submit">THÊM</button>
                </div>
            </form>
        </div>
    </body>
</html>
