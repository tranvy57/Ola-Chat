package vn.edu.iuh.fit.olachatbackend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("🔐 Mã Xác Thực OTP - OlaChat Social");

            String emailContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; padding: 20px; " +
                    "border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;'>"
                    + "<h2 style='color: #333;'>Xin chào,</h2>"
                    + "<p>Bạn vừa yêu cầu mã OTP để xác thực tài khoản trên <strong>OlaChat</strong>.</p>"
                    + "<p><strong>Mã OTP của bạn:</strong></p>"
                    + "<h2 style='text-align: center; color: #d9534f; background-color: #fbeaea; padding: 10px; " +
                    "border-radius: 5px;'>" + otp + "</h2>"
                    + "<p><strong>Lưu ý:</strong> Mã OTP này có hiệu lực trong <strong>5 phút</strong>. " +
                    "Vui lòng không chia sẻ mã này với bất kỳ ai.</p>"
                    + "<p>Nếu bạn không yêu cầu OTP này, vui lòng bỏ qua email này.</p>"
                    + "<hr style='border: none; border-top: 1px solid #ddd;'>"
                    + "<p style='text-align: center; font-size: 14px; color: #555;'>"
                    + "Trân trọng,<br><strong>Đội ngũ OlaChat</strong></p>"
                    + "<p style='text-align: center; font-size: 12px; color: #777;'>"
                    + "Email này được gửi tự động. Vui lòng không trả lời email này.</p>"
                    + "</div>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Không thể gửi email: {}", e.getMessage());
            throw new RuntimeException("Không thể gửi email: " + e.getMessage());
        }
    }

    public void sendVerifyNewEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("📧 Xác Thực Email Mới - OlaChat");

            String emailContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; padding: 20px; " +
                    "border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;'>"
                    + "<h2 style='color: #333;'>Xin chào,</h2>"
                    + "<p>Bạn đang yêu cầu <strong>cập nhật email</strong> cho tài khoản trên <strong>OlaChat</strong>.</p>"
                    + "<p><strong>Đây là mã xác thực để xác nhận email mới của bạn:</strong></p>"
                    + "<h2 style='text-align: center; color: #0275d8; background-color: #eaf4fb; padding: 10px; " +
                    "border-radius: 5px;'>" + otp + "</h2>"
                    + "<p><strong>Lưu ý:</strong> Mã OTP có hiệu lực trong <strong>5 phút</strong>. Vui lòng không chia sẻ mã này.</p>"
                    + "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>"
                    + "<hr style='border: none; border-top: 1px solid #ddd;'>"
                    + "<p style='text-align: center; font-size: 14px; color: #555;'>"
                    + "Trân trọng,<br><strong>Đội ngũ OlaChat</strong></p>"
                    + "<p style='text-align: center; font-size: 12px; color: #777;'>"
                    + "Email này được gửi tự động. Vui lòng không phản hồi lại email.</p>"
                    + "</div>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Không thể gửi email xác thực cập nhật email: {}", e.getMessage());
            throw new RuntimeException("Không thể gửi email xác thực cập nhật email: " + e.getMessage());
        }
    }

}
