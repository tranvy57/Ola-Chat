package vn.edu.iuh.fit.olachatbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Reset Password - OTP Verification");
            message.setText("Your OTP code is: " + otpCode + "\nThis code will expire in 5 minutes.");

            mailSender.send(message);
            log.info("✅ Email OTP đã gửi thành công đến: {}", toEmail);
        } catch (MailException e) {
            log.error("❌ Gửi email thất bại: {}", e.getMessage());
            throw new RuntimeException("Lỗi khi gửi email. Vui lòng thử lại!");
        }
    }
}
