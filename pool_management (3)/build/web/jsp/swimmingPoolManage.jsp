<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý hồ bơi</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/swimmingPoolManage.css?v=<%= System.currentTimeMillis() %>">
    </head>
    <body>
        <header>
            <jsp:include page="header.jsp"></jsp:include>
            </header>
            <div class="container">
                <h1>Quản lý hồ bơi</h1>

                <!-- Tìm kiếm và Lọc -->
                <div class="search-filter">
                    <form action="${pageContext.request.contextPath}/SwimmingPoolManageServlet" method="get">
                    <input type="text" name="keyword" placeholder="Tìm theo tên hoặc địa điểm" value="${param.keyword}">
                    <select name="status">
                        <option value="">Tất cả trạng thái</option>
                        <option value="active" ${param.status == 'active' ? 'selected' : ''}>Hoạt động</option>
                        <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                    </select>
                    <button type="submit">Tìm kiếm</button>
                </form>
                <button onclick="showAddForm()">Thêm hồ bơi</button>
            </div>

            <!-- Thông báo lỗi -->
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <!-- Danh sách hồ bơi -->
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên</th>
                        <th>Địa điểm</th>
                        <th>Số điện thoại</th>
                        <th>Trang fanpage</th>
                        <th>Giờ mở cửa</th>
                        <th>Giờ đóng cửa</th>
                        <th>Trạng thái</th>
                        <th>Hình ảnh</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pool" items="${poolList}">
                        <tr>
                            <td>${pool.poolID}</td>
                            <td><c:out value="${pool.name}" /></td>
                            <td><c:out value="${pool.location}" /></td>
                            <td><c:out value="${pool.phone}" /></td>
                            <td><c:out value="${pool.fanpage}" /></td>
                            <td><c:out value="${pool.openTime}" /></td>
                            <td><c:out value="${pool.closeTime}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${pool.status}">
                                        <span class="active">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="inactive">Không hoạt động</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${not empty pool.image}">
                                    <img src="${pageContext.request.contextPath}/images/pools/${pool.image}" alt="Hình ảnh hồ bơi" width="100">
                                </c:if>
                            </td>
                            <td>
                                <a href="#" onclick="showEditForm(
                                                '${pool.poolID}',
                                                '${pool.name.replace('\'', '\\\'')}',
                                                '${pool.location.replace('\'', '\\\'')}',
                                                '${pool.phone.replace('\'', '\\\'')}',
                                                '${pool.fanpage.replace('\'', '\\\'')}',
                                                '${pool.openTime}',
                                                '${pool.closeTime}',
                                                '${pool.description != null ? pool.description.replace('\'', '\\\'') : ''}',
                                                '${pool.image != null ? pool.image.replace('\'', '\\\'') : ''}',
                                                '${pool.status ? 'true' : 'false'}'
                                                )" class="action-btn edit-btn">Sửa</a>
                                <a href="${pageContext.request.contextPath}/SwimmingPoolManageServlet?action=delete&poolID=${pool.poolID}" 
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa hồ bơi này không?')" 
                                   class="action-btn delete-btn">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Form thêm hồ bơi -->
            <div id="addForm" class="form-modal">
                <div class="form-content">
                    <button type="button" class="close-btn" onclick="closeAddForm()">×</button>
                    <h2>Thêm hồ bơi mới</h2>
                    <form id="addPoolForm" action="${pageContext.request.contextPath}/SwimmingPoolManageServlet" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="add">
                        <label>Tên: <input type="text" name="name" required></label>
                        <label>Địa điểm: <input type="text" name="location" required></label>
                        <label>Số điện thoại: <input type="text" name="phone" required></label>
                        <label>Trang fanpage: <input type="text" name="fanpage"></label>
                        <label>Giờ mở cửa: <input type="time" name="openTime" required></label>
                        <label>Giờ đóng cửa: <input type="time" name="closeTime" required></label>
                        <label>Mô tả: <textarea name="description"></textarea></label>
                        <label>Hình ảnh: <input type="file" name="image" accept="image/*"></label>
                        <label>Trạng thái: 
                            <select name="status">
                                <option value="true">Hoạt động</option>
                                <option value="false">Không hoạt động</option>
                            </select>
                        </label>
                        <button type="submit">Lưu</button>
                        <button type="button" onclick="closeAddForm()">Hủy</button>
                    </form>
                </div>
            </div>

            <!-- Form sửa hồ bơi -->
            <div id="editForm" class="form-modal">
                <div class="form-content">
                    <button type="button" class="close-btn" onclick="closeEditForm()">×</button>
                    <h2>Sửa hồ bơi</h2>
                    <form id="editPoolForm" action="${pageContext.request.contextPath}/SwimmingPoolManageServlet" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" id="editPoolID" name="poolID">
                        <label>Tên: <input type="text" id="editName" name="name" required></label>
                        <label>Địa điểm: <input type="text" id="editLocation" name="location" required></label>
                        <label>Số điện thoại: <input type="text" id="editPhone" name="phone" required></label>
                        <label>Trang fanpage: <input type="text" id="editFanpage" name="fanpage"></label>
                        <label>Giờ mở cửa: <input type="time" id="editOpenTime" name="openTime" required></label>
                        <label>Giờ đóng cửa: <input type="time" id="editCloseTime" name="closeTime" required></label>
                        <label>Mô tả: <textarea id="editDescription" name="description"></textarea></label>
                        <label>Hình ảnh: <input type="file" id="editImage" name="image" accept="image/*"></label>
                        <label>Trạng thái: 
                            <select name="status" id="editStatus">
                                <option value="true">Hoạt động</option>
                                <option value="false">Không hoạt động</option>
                            </select>
                        </label>
                        <button type="submit">Cập nhật</button>
                        <button type="button" onclick="closeEditForm()">Hủy</button>
                    </form>
                </div>
            </div>

            <script>
                function showAddForm() {
                    const addForm = document.getElementById("addForm");
                    addForm.classList.add("active");
                    document.getElementById("addPoolForm").reset();
                }

                function closeAddForm() {
                    const addForm = document.getElementById("addForm");
                    addForm.classList.remove("active");
                }

                function showEditForm(poolID, name, location, phone, fanpage, openTime, closeTime, description, image, status) {
                    document.getElementById("editPoolID").value = poolID || "";
                    document.getElementById("editName").value = name || "";
                    document.getElementById("editLocation").value = location || "";
                    document.getElementById("editPhone").value = phone || "";
                    document.getElementById("editFanpage").value = fanpage || "";
                    document.getElementById("editOpenTime").value = openTime ? openTime.substring(0, 5) : "";
                    document.getElementById("editCloseTime").value = closeTime ? closeTime.substring(0, 5) : "";
                    document.getElementById("editDescription").value = description || "";
                    document.getElementById("editImage").value = ""; // Reset file input
                    document.getElementById("editStatus").value = status; // Set status value
                    const editForm = document.getElementById("editForm");
                    editForm.classList.add("active");
                }

                function closeEditForm() {
                    const editForm = document.getElementById("editForm");
                    editForm.classList.remove("active");
                }
            </script>
    </body>
</html>