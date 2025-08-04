/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author THIS PC
 */
public class SendEmailDAO extends DBContext{
    public void sendEmail(String toEmail,int UserID, String contextPath) throws UnsupportedEncodingException {
        String fromEmail = "xumlmhe186012@fpt.edu.vn"; // Thay bằng email của bạn
        String password = "ebxe blkm hzrs teni"; // Thay bằng mật khẩu hoặc App Password

        // Cấu hình SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Tạo session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            String subject = MimeUtility.encodeText("Reset password", "UTF-8", "B");
            message.setSubject(subject);

            // Nội dung email (HTML

            String link = "http://localhost:9999" + contextPath + "/ServletPasswordReset?service=CheckReset";
            String emailBody = "<p>Click the following link to reset your password: <a href='" + link + "'>Reset Password</a>\"</p>";
                    
            message.setContent(emailBody, "text/html; charset=utf-8");

            // Gửi email
            Transport.send(message);
            System.out.println("Email đã được gửi đến " + toEmail);

        } catch (MessagingException e) {
            System.out.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
        public void sendEmail1(String toEmail, String subject, String bodyHtml) throws UnsupportedEncodingException {
    String fromEmail = "xumlmhe186012@fpt.edu.vn"; // Email của bạn
    String password = "ebxe blkm hzrs teni";       // App password
    // Cấu hình SMTP
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");
    // Tạo session
    Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fromEmail, password);
        }
    });
    try {
        // Tạo email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail, "Swim Center Support")); // Tên hiển thị
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        // Encode tiêu đề để không lỗi Unicode
        String encodedSubject = MimeUtility.encodeText(subject, "UTF-8", "B");
        message.setSubject(encodedSubject);
        // Gán nội dung HTML
        message.setContent(bodyHtml, "text/html; charset=utf-8");
        // Gửi email
        Transport.send(message);
        System.out.println("Email đã được gửi đến " + toEmail);
    } catch (MessagingException e) {
        System.out.println("Lỗi khi gửi email: " + e.getMessage());
    }
}

}
