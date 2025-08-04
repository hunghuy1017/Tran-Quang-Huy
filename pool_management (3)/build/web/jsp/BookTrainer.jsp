<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Employee, models.Classes, java.util.List" %>
<%
    Employee trainer = (Employee) request.getAttribute("trainer");
    List<Classes> classList = (List<Classes>) request.getAttribute("classList");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Đặt lịch Huấn luyện viên</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/BookTrainer.css">
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="form-container">
            <h2>Đặt lịch Huấn luyện viên</h2>

            <div class="trainer-detail">
                <p><strong>Họ tên HLV:</strong> <%= trainer.getFullName() %></p>
                <p><strong>Email:</strong> <%= trainer.getEmail() %></p>
                <p><strong>Điện thoại:</strong> <%= trainer.getPhone() %></p>
                <p><strong>Hồ bơi:</strong> <%= trainer.getPoolName() %></p>
                <p><strong>Vị trí:</strong> <%= trainer.getPosition() != null ? trainer.getPosition() : "Huấn luyện viên" %></p>
            </div>

            <form action="book-trainer" method="post">
                <input type="hidden" name="trainerID" value="<%= trainer.getUserID() %>">
                <input type="hidden" name="poolID" value="<%= trainer.getPoolID() %>">
                <input type="hidden" name="userID" value="1"> <%-- TODO: Lấy từ session --%>
                <input type="hidden" name="ngayBatDau" id="ngayBatDau"> <%-- 👈 THÊM DÒNG NÀY --%>

                <label for="hoTen">Họ và tên học viên</label>
                <input type="text" name="hoTen" id="hoTen" required>

                <label for="tuoi">Tuổi</label>
                <input type="number" name="tuoi" id="tuoi" required>

                <label for="classId">Chọn lớp học</label>
                <select name="classID" id="classId" onchange="showClassInfo(this)" required>
                    <option value="">-- Chọn lớp --</option>
                    <% for (Classes c : classList) {
                        boolean isFull = c.getEnrolledCount() >= c.getMaxParticipants();
                    %>
                    <option 
                        value="<%= c.getClassID() %>"
                        data-date="<%= c.getClassDate() %>"
                        data-start="<%= c.getStartTime() %>"
                        data-end="<%= c.getEndTime() %>"
                        data-desc="<%= c.getDescription() %>"
                        data-max="<%= c.getMaxParticipants() %>"
                        data-current="<%= c.getEnrolledCount() %>"
                        data-price="<%= c.getPrice() %>"
                        <%= isFull ? "disabled" : "" %>
                        >
                        <%= c.getClassName() %> - <%= c.getPrice() %> VNĐ
                        <% if (isFull) { %> [Đã đủ học viên] <% } else { %> 
                        [<%= c.getEnrolledCount() %> / <%= c.getMaxParticipants() %> HV] 
                        <% } %>
                    </option>
                    <% } %>
                </select>

                <div id="class-info">
                    <p><strong>Ngày học:</strong> <span id="class-date">-</span></p>
                    <p><strong>Thời gian:</strong> <span id="class-time">-</span></p>
                    <p><strong>Mô tả:</strong> <span id="class-desc">-</span></p>
                    <p><strong>Học viên:</strong> <span id="class-current">-</span>/<span id="class-max">-</span></p>
                    <p><strong>Giá:</strong> <span id="class-price">-</span> VNĐ</p>
                </div>

                <label for="mucTieu">Mục tiêu cá nhân</label>
                <input type="text" name="mucTieu" id="mucTieu">

                <button type="submit">Xác nhận đặt lịch</button>
            </form>
        </div>

        <script>
            function showClassInfo(select) {
                const opt = select.options[select.selectedIndex];
                document.getElementById('class-date').innerText = opt.dataset.date || "-";
                document.getElementById('class-time').innerText = (opt.dataset.start || "") + " - " + (opt.dataset.end || "");
                document.getElementById('class-desc').innerText = opt.dataset.desc || "-";
                document.getElementById('class-max').innerText = opt.dataset.max || "-";
                document.getElementById('class-current').innerText = opt.dataset.current || "0";
                document.getElementById('class-price').innerText = opt.dataset.price || "-";

                // 👇 Gán giá trị cho input ẩn
                document.getElementById('ngayBatDau').value = opt.dataset.date || "";
                document.getElementById('price').value = opt.dataset.price || "";
            }

        </script>

    </body>
</html>



