<%-- 
    Document   : ChangPassword
    Created on : Jun 21, 2025, 11:41:04 AM
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chang Password Page</title>
         <style>
        /* Thiết lập font chữ và nền cho toàn trang */
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        /* Container chính cho form, tạo hiệu ứng bo tròn và đổ bóng */
        .form-container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 400px;
            border: 1px solid #ddd;
        }

        /* Tiêu đề của form */
        .form-title {
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin-bottom: 30px;
        }

        /* Nhóm chứa label và input */
        .form-group {
            margin-bottom: 20px;
        }

        /* Style cho các nhãn (label) */
        .form-group label {
            display: block;
            font-weight: 600;
            color: #555;
            margin-bottom: 8px;
        }

        /* Style chung cho các ô input */
        .form-group input[type="password"],
        .form-group input[type="text"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-sizing: border-box; /* Quan trọng để padding không làm thay đổi kích thước */
            font-size: 16px;
        }
        
        /* Nhóm riêng cho Captcha để sắp xếp ngang hàng */
        .captcha-group {
            display: flex;
            align-items: center;
            gap: 15px; /* Khoảng cách giữa ảnh captcha và ô input */
        }
        
        /* Box mô phỏng ảnh Captcha */
        .captcha-image {
            width: 150px;
            height: 50px;
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: #f9f9f9;
            display: flex;
            justify-content: center;
            align-items: center;
            font-style: italic;
            color: #888;
            user-select: none; /* Ngăn người dùng chọn văn bản */
        }

        .captcha-group input {
            flex-grow: 1; /* Ô input sẽ chiếm hết phần không gian còn lại */
        }

.btn {
    display: inline-block;
    margin-top: 10px;
    padding: 10px 20px;
    background: #00796b;
    color: white;
    text-decoration: none;
    border-radius: 5px;
}

.btn:hover {
    background: #004d40;
}
        /* Nút "Xác nhận" */
        .submit-btn {
            width: 100%;
            padding: 12px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 10px;
            transition: background-color 0.3s ease;
        }

        .submit-btn:hover {
            background-color: #0056b3;
        }
    </style>
    </head>
    <body>

    <div class="form-container">
        <div class="form">
        <h2 class="form-title">Thay Đổi Mật Khẩu</h2>
        <form id="changepass-form" action="ChangPassword" method="POST">
            <div class="form-group">
                <label for="old-password">Mật Khẩu cũ</label>
                <input type="password" id="old-password" name="old_password" required>
            </div>
            <div class="form-group">
                <label for="new-password">Mật khẩu mới</label>
                <input type="password" id="new-password" name="new_password" required>
            </div>
            <div class="form-group">
                <label for="confirm-password">Xác nhận Mật khẩu mới</label>
                <input type="password" id="confirm-password" name="confirm_password" required>
            </div>
            <div class="form-group">
                <label for="captcha">Captcha</label>
                  <div class="g-recaptcha" data-sitekey="6LfGT2grAAAAAFv_j46dA1qgfVscAvHLwNiKAs9Q"></div>
                  <div style="color: red" id="error"></div>
            </div>
            <div style="color: red"><c:if  test="${not empty mess}" >${mess}</c:if></div>
            <button type="button" onclick="checkcapcha()" class="submit-btn">Xác nhận</button>
        </form>
        </div>
            <a href="home" class="btn">Trở Về</a>
    </div>
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
<script type="text/javascript">
    function checkcapcha(){
        var form = document.getElementById("changepass-form");
        var  error = document.getElementById("error");
        var oldPass = document.getElementById("old-password").value.trim();
        var newPass = document.getElementById("new-password").value.trim();
        var confirmPass = document.getElementById("confirm-password").value.trim();
        const response = grecaptcha.getResponse();
         error.textContent = "";
        if (!oldPass || !newPass || !confirmPass) {
            error.textContent = "Vui lòng nhập đầy đủ các trường mật khẩu.";
            return;
        }
        if(response){
            form.submit();
        }else{
            error.textContent = "xác thực bạn không phải robot";
        }
    }
</script>
</body>
</html>
