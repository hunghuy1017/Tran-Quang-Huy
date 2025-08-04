<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>User Review</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="../css/review.css">
</head>
<body>

  <!-- Header -->
  <jsp:include page="header.jsp"></jsp:include>

  <div class="container">
    <div class="top-section">
      <button class="btn-back">Back</button>

      <div class="profile-info">
        <div class="profile-pic"></div>
        <h3>Name</h3>
        <p>5 out of 5</p>
        <div class="stars">
          <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>
          <i class="fas fa-star"></i><i class="fas fa-star"></i>
        </div>
      </div>

      <div class="review-form">
        <textarea placeholder="Enter your review"></textarea>
        <div class="form-stars">
          <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>
          <i class="fas fa-star"></i><i class="fas fa-star"></i>
        </div>
        <button class="btn-submit">Evaluate</button>
      </div>
    </div>

    <div class="filter-buttons">
      <button class="active">ALL</button>
      <button>5 stars</button>
      <button>4 stars</button>
      <button>3 stars</button>
      <button>2 stars</button>
      <button>1 star</button>
    </div>

    <div class="review-list">
      <div class="review-item">
        <div class="user-info">
          <i class="fas fa-user-circle user-icon"></i>
          <div>
            <strong>Name</strong>
            <div class="date">dd/mm/yyyy</div>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit...</p>
          </div>
        </div>
        <div class="stars">
          <i class="fas fa-star"></i><i class="fas fa-star"></i>
          <i class="fas fa-star"></i><i class="fas fa-star"></i>
          <i class="fas fa-star"></i>
        </div>
      </div>
        
        <div class="review-list">
      <div class="review-item">
        <div class="user-info">
          <i class="fas fa-user-circle user-icon"></i>
          <div>
            <strong>Name</strong>
            <div class="date">dd/mm/yyyy</div>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit...</p>
          </div>
        </div>
        <div class="stars">
          <i class="fas fa-star"></i><i class="fas fa-star"></i>
          <i class="fas fa-star"></i><i class="fas fa-star"></i>
          <i class="fas fa-star"></i>
        </div>
      </div>

      
    </div>

    <div class="pagination">
      <button class="active">1</button>
      <button>2</button>
      <button>3</button>
      <button>&gt;</button>
    </div>      
  </div>
     </div>
  <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
