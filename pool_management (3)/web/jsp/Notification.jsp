<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Notifications</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification.css">
    </head>
    <body>

        <header>
            <jsp:include page="header.jsp"></jsp:include>
            </header>

            <div class="notifications-container">
                <div class="header-row">
                    <form action="ServletNotification" method="get">
                        <div class="header-left">
                            <h2>THÔNG BÁO</h2>

                            <select name="status" onchange="this.form.submit()">
                                <option value="all" ${param.status == 'all' ? 'selected' : ''}>Tất cả</option>
                            <option value="read" ${param.status == 'read' ? 'selected' : ''}>Đọc</option>
                            <option value="unread" ${param.status == 'unread' ? 'selected' : ''}>Chưa đọc</option>
                        </select>

                        <select name="sort" onchange="this.form.submit()">
                            <option value="desc" ${param.sort == 'desc' ? 'selected' : ''}>Mới nhất</option>
                            <option value="asc" ${param.sort == 'asc' ? 'selected' : ''}>Cũ nhất</option>
                        </select>

                        <input type="hidden" name="Title" value="${Title}">
                        <input type="hidden" name="service" value="listNotification">
                    </div>
                </form>

                <form action="ServletNotification">
                    <div class="filters">
                        <input type="text" name="Title" value="${Title}" placeholder="Tìm kiếm...">
                        <button type="submit" name="submit">Tìm kiếm</button>
                        <input type="hidden" name="service" value="listNotification">
                    </div>
                </form>
            </div>
            <c:if test="${mess != null}">
                <div class="success-message">
                    <i class="fa-solid fa-circle-check"></i> ${mess}
                </div>
            </c:if>
            <c:forEach items="${data}" var="Notification">
                <div class="notification-list">
                    <div class="notification">
                        <div class="text">
                            <a href="ServletNotification?service=NotificationDetail&nid=${Notification.notificationID}" style="text-decoration: none; color: inherit;">
                                ${Notification.title}
                            </a>
                        </div>
                        <div class="date">${Notification.createdAt}</div>
                        <div class="actions">
                            <c:if test="${Notification.isRead == true}">
                                <img src="${pageContext.request.contextPath}/images/check.png" alt="Mark as read" title="Mark as read">
                            </c:if>
                            <a href="#"onclick="showMess(${Notification.notificationID})"><img src="${pageContext.request.contextPath}/images/delete.png" alt="Delete" title="Delete"></a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <div class="pagination">
                <c:if test="${page > 1}">
                    <a href="ServletNotification?service=listNotification&page=${page - 1}&Title=${Title}&status=${status}&sort=${sort}">&lt;</a>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="ServletNotification?service=listNotification&page=${i}&Title=${Title}&status=${status}&sort=${sort}" class="${page == i ? 'active' : ''}">${i}</a>
                </c:forEach>

                <c:if test="${page < totalPages}">
                    <a href="ServletNotification?service=listNotification&page=${page + 1}&Title=${Title}&status=${status}&sort=${sort}">&gt;</a>
                </c:if>
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
</script>

</html>
