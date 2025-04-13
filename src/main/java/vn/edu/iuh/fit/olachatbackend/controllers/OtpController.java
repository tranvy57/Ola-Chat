package vn.edu.iuh.fit.olachatbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.MessageResponse;
import vn.edu.iuh.fit.olachatbackend.services.OtpServiceFactory;
import vn.edu.iuh.fit.olachatbackend.utils.FormatPhoneNumber;

import java.util.Map;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpServiceFactory otpServiceFactory;

    @PostMapping("/send")
    public MessageResponse<Void> sendOtp(@RequestBody Map<String, String> request) {
        String phone = FormatPhoneNumber.formatPhoneNumberTo84(request.get("phone"));
        String provider = request.getOrDefault("provider", "twilio"); // twilio mặc định nếu không truyền

        otpServiceFactory.getStrategy(provider).sendOtp(phone);

        return MessageResponse.<Void>builder()
                .message("✅ Gửi OTP thành công đến số: " + phone + " bằng " + provider)
                .success(true)
                .data(null)
                .build();
    }

    @PostMapping("/verify")
    public ResponseEntity<MessageResponse<Void>> verifyOtp(@RequestBody Map<String, String> request) {
        String phone = FormatPhoneNumber.formatPhoneNumberTo84(request.get("phone"));
        String otp = request.get("otp");
        String provider = request.getOrDefault("provider", "twilio");

        boolean isValid = otpServiceFactory.getStrategy(provider).verifyOtp(phone, otp);

        if (isValid) {
            return ResponseEntity.ok(MessageResponse.<Void>builder()
                    .message("✅ Xác thực OTP thành công!")
                    .success(true)
                    .data(null)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.<Void>builder()
                            .message("❌ OTP không đúng hoặc đã hết hạn.")
                            .success(false)
                            .data(null)
                            .build());
        }
    }
}
