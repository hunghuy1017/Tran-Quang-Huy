
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notificationdetail.css">
        <title>JSP Page</title>
    </head>
    <body>

        <header>
            <jsp:include page="header.jsp"></jsp:include>
            </header>

            <div class="container">
                <button onclick="goBack()" class="back-button">Quạy lại</button>

                <div class="title">${N.title}</div>

            <div class="meta-info">
                <span>Ngày nhận: ${N.createdAt}</span>
            </div>

            <div class="content-title">Nội dung chi tiết:</div>
            <div class="content-box">
                ${N.message}
            </div>

            <div class="actions">
                <c:if test="${not empty Mess}">
                    <p style="color: #00cc33">${Mess}</p>
                </c:if>

                <a href="ServletNotification?service=CheckNotification&nid=${N.notificationID}&IsRead=${N.isRead}"><img src="${pageContext.request.contextPath}/images/check.png" alt="Edit" class="icon"></a>
                <a href="#" onclick="showMess(${N.notificationID})"><img src="${pageContext.request.contextPath}/images/delete.png" alt="Delete" class="icon delete"></a>
            </div>
        </div>
        <div id="confirm-popup" class="confirm-popup hidden">
            <div class="confirm-box">
                <p>Bạn có chắc chắn muốn xóa thông báo này không?</p>
                <div class="buttons">
                    <button id="confirm-yes">Có</button>
                    <button id="confirm-no">Không</button>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>

    </body>
    <script>
        let deleteId = null;

        function showMess(notificationID) {
            deleteId = notificationID;
            document.getElementById('confirm-popup').classList.remove('hidden');
        }

        document.getElementById('confirm-yes').addEventListener('click', function () {
            if (deleteId !== null) {
                window.location.href = 'ServletNotification?service=DeleteNotification&nid=' + deleteId;
            }
        });

        document.getElementById('confirm-no').addEventListener('click', function () {
            document.getElementById('confirm-popup').classList.add('hidden');
            deleteId = null;
        });

        function goBack() {
            window.history.back();
        }
    </script>
</html>
