<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/AddProduct.css">
        <title>Cập Nhật Sản Phẩm</title>
    </head>
    <body>
        <div class="form-container">
            <h2>Cập Nhật Sản Phẩm</h2>

            <div class="back-btn">
                <button><a href="ServletWarehouseManager">QUAY LẠI</a></button>
            </div>
            <form action="ServletWarehouseManager?service=UpdateProduct" method="post" enctype="multipart/form-data">
                <input type="hidden" name="pID" value="${product.productID}" />

                <div class="form-row">
                    <div class="form-group">
                        <label>Bể Bơi</label>
                        <c:choose>
                            <c:when test="${position == 'Manager' || position == 'Warehouse Management'}">
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
                                    <c:forEach var="p" items="${pools}">
                                        <option value="${p.poolID}" ${p.poolID == product.poolID ? 'selected' : ''}>${p.name}</option>
                                    </c:forEach>
                                </select>
                            </c:otherwise>
                        </c:choose>

                    </div>
                    <div class="form-group">
                        <label>Tên Sản Phẩm</label>
                        <input type="text" name="productName" value="${product.productName}"
                               ${position == 'Warehouse Management' ? 'readonly' : 'required'} />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Mô Tả</label>
                        <input type="text" name="description" value="${product.description} " 
                               ${position == 'Warehouse Management' ? 'readonly' : 'required'}/>
                    </div>
                    <div class="form-group">
                        <label>Giá</label>
                        <input type="number" step="0.01" name="price" value="${product.price}" 
                               ${position == 'Warehouse Management' ? 'readonly' : 'required'}/>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Ngày Nhập</label>
                        <input type="datetime-local" name="addedDate" value="${product.addedDate}" 
                               ${position == 'Warehouse Management' ? 'readonly' : 'required'}/>
                    </div>
                    <div class="form-group">
                        <label>Số Lượng</label>
                        <input type="number" name="quantity" value="${product.quantity}"/>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <c:choose>
                            <c:when test="${position == 'Warehouse Management'}">
                                <label>Hình Ảnh</label>
                                <input type="file" name="image" accept="image/*"
                                       ${position == 'Warehouse Management' ? 'disabled' : ''}/>
                                <input type="hidden" name="image" value="${product.image}" />                 
                            </c:when>
                            <c:otherwise>
                                <label>Hình Ảnh</label>
                                <input type="file" name="image" accept="image/*" onchange="checkFileSize(this)"/>
                                <script>
                                    function checkFileSize(input) {
                                        const maxSize = 2 * 1024 * 1024; // 500KB in bytes
                                        if (input.files.length > 0 && input.files[0].size > maxSize) {
                                            alert("File quá lớn! Vui lòng chọn file nhỏ hơn 2mbMB.");
                                            input.value = ""; // Xóa file đã chọn
                                        }
                                    }
                                </script>
                            </c:otherwise>
                        </c:choose>


                    </div>
                    <div class="form-group">
                        <div class="form-group">
                            <label>Giá thuê</label>
                            <input type="number" step="0.01" name="RentalPrice" value="${product.rentalPrice}" 
                                   ${position == 'Warehouse Management' ? 'readonly' : 'required'}/>
                        </div>                       
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Danh Mục</label>
                        <select name="Categories"
                                ${position == 'Warehouse Management' ? 'disabled' : 'required'}>
                            <c:forEach var="c" items="${Categories}">
                                <option value="${c.categoryID}"
                                        ${c.categoryID == product.categoryID ? 'selected' : ''}>
                                    ${c.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                        <input type="hidden" name="Categories" value="${product.categoryID}" />
                    </div>

                    <div class="form-group">
                        <label>Cho thuê</label>
                        <select name="IsRentable"
                                ${position == 'Warehouse Management' ? 'disabled' : 'required'}>
                            <option value="true" ${product.isRentable ? 'selected' : ''}>Có</option>
                            <option value="false" ${!product.isRentable ? 'selected' : ''}>Không</option>
                        </select>
                        <input type="hidden" name="IsRentable" value="${product.isRentable}" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Trạng Thái</label>                        
                        <div class="status-group">
                            <c:choose>
                                <c:when test="${position == 'Warehouse Management'}">
                                    <!-- Hiển thị radio bị disabled + giữ lại giá trị bằng hidden input -->
                                    <div class="status-option green">
                                        <input type="radio" id="forSale" value="true"
                                               ${product.status ? "checked" : ""} disabled />
                                        <label for="forSale">Đang Bán</label>
                                    </div>
                                    <div class="status-option red">
                                        <input type="radio" id="stopSelling" value="false"
                                               ${!product.status ? "checked" : ""} disabled />
                                        <label for="stopSelling">Ngừng Bán</label>
                                    </div>
                                    <!-- Hidden input để vẫn gửi được trạng thái -->
                                    <input type="hidden" name="status" value="${product.status}" />
                                </c:when>
                                <c:otherwise>
                                    <div class="status-option green">
                                        <input type="radio" id="forSale" name="status" value="true"
                                               ${product.status ? "checked" : ""} required />
                                        <label for="forSale">Đang Bán</label>
                                    </div>
                                    <div class="status-option red">
                                        <input type="radio" id="stopSelling" name="status" value="false"
                                               ${!product.status ? "checked" : ""} required />
                                        <label for="stopSelling">Ngừng Bán</label>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>


                <div class="submit-btn">
                    <button name="submit" type="submit">CẬP NHẬT</button>
                </div>
            </form>
        </div>
    </body>
</html>
