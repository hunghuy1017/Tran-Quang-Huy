<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>User Review</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reviewpool.css">
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="header.jsp"></jsp:include>
            <div class="container">                
                <div class="top-section">
                    <button onclick="goBack()" class="btn-back">Quay lại</button>
                    <div class="profile-info">
                        <div class="profile-pic"><img src="${pageContext.request.contextPath}/images/trainer/${sp.image}" alt="Mark as read" title="Mark as read"></div>
                    <h3>${sp.fullName}</h3>
                    <p><fmt:formatNumber value="${avg}" maxFractionDigits="1"/> trên 5</p>
                    <div class="stars">
                        <c:forEach begin="1" end="${fullStars}">
                            <i class="fas fa-star"></i>
                        </c:forEach>
                        <c:if test="${hasHalfStar}">
                            <i class="fas fa-star-half-alt"></i>
                        </c:if>
                        <c:forEach begin="1" end="${emptyStars}">
                            <i class="far fa-star"></i>
                        </c:forEach>
                    </div>

                </div>

                <c:choose>
                    <c:when test="${empty mess}">
                        <form action="ServletTrainerReview" method="post" class="review-form">
                            <input type="hidden" name="service" value="addReview"/>
                            <input type="hidden" name="userID" value="${user.userID}"/>
                            <input type="hidden" name="TrainerID" value="${sp.userID}"/>

                            <textarea name="comment" placeholder="Nhập đánh giá cảu ban..."></textarea>
                            <select name="rating">
                                <option value="5">&#9733;&#9733;&#9733;&#9733;&#9733; - Tuyệt vời</option>
                                <option value="4">&#9733;&#9733;&#9733;&#9733; - Tốt</option>
                                <option value="3">&#9733;&#9733;&#9733; - Trung bình </option>
                                 <option value="2">&#9733;&#9733; - Tệ</option>
                                <option value="1">&#9733; - Rất tệ</option>
                            </select>
                            <button type="submit" class="btn-submit">Evaluate</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="review-message">${mess}</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="filter-buttons">
                <a href="ServletTrainerReview?service=listUserReviewTrainer&TrainerID=${sp.userID}&star=0" class="${star == 0 ? 'active' : ''}">Tất cả</a>
                <a href="ServletTrainerReview?service=listUserReviewTrainer&TrainerID=${sp.userID}&star=5" class="${star == 5 ? 'active' : ''}">5 sao</a>
                <a href="ServletTrainerReview?service=listUserReviewTrainer&TrainerID=${sp.userID}&star=4" class="${star == 4 ? 'active' : ''}">4 sao</a>
                <a href="ServletTrainerReview?service=listUserReviewTrainer&TrainerID=${sp.userID}&star=3" class="${star == 3 ? 'active' : ''}">3 sao</a>
                <a href="ServletTrainerReview?service=listUserReviewTrainer&TrainerID=${sp.userID}&star=2" class="${star == 2 ? 'active' : ''}">2 sao</a>
                <a href="ServletTrainerReview?service=listUserReviewTrainer&TrainerID=${sp.userID}&star=1" class="${star == 1 ? 'active' : ''}">1 sao</a>
            </div>
            <div class="review-list">
                <c:forEach var="r" items="${reviews}">
                    <div class="review-item">
                        <div class="user-info">
                            <img class="user-icon" src="${pageContext.request.contextPath}/images/users/${r.userImage}" alt="User Image" style="width:40px; height:40px; border-radius:50%;">
                            <div>
                                <strong>${r.userName}</strong>
                                <div class="date">${r.createdAt}</div>
                                <p>${r.comment}</p>
                            </div>
                        </div>

                        <div class="stars">
                            <c:forEach begin="1" end="${r.rating}">
                                <i class="fas fa-star"></i>
                            </c:forEach>
                            <c:forEach begin="1" end="${5 - r.rating}">
                                <i class="far fa-star"></i>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>

                <div class="pagination">
                    <c:if test="${page > 1}">
                        <a href="ServletTrainerReview?service=listUserReviewTrainer&page=${page - 1}&TrainerID=${sp.userID}&star=${star}">&lt;</a>
                    </c:if>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="ServletTrainerReview?service=listUserReviewTrainer&TrainerID=${sp.userID}&star=${star}&page=${i}" class="${page == i ? 'active' : ''}">${i}</a>
                    </c:forEach>
                    <c:if test="${page < totalPages}">
                        <a href="ServletTrainerReview?service=listUserReviewTrainer&page=${page + 1}&TrainerID=${sp.userID}&star=${star}">&gt;</a>
                    </c:if>
                </div>

            </div>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
    <script>
    function goBack() {
        window.history.back();
    }
</script>
</html>
