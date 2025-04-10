package vn.edu.iuh.fit.olachatbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.MessageResponse;
import vn.edu.iuh.fit.olachatbackend.services.TwilioService;
import vn.edu.iuh.fit.olachatbackend.utils.FormatPhoneNumber;

import java.util.Map;

@RestController
@RequestMapping("/twilio")
@RequiredArgsConstructor
public class TwilioController {

    private final TwilioService twilioService;

    @PostMapping("/send-otp")
    public MessageResponse<Void> sendOtp(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String formattedPhone = FormatPhoneNumber.formatPhoneNumberTo84(phone);
        twilioService.sendOtp(formattedPhone);
        return MessageResponse.<Void>builder()
                .message("Gửi SMS OTP thành công đến số điện thoại: " + formattedPhone)
                .data(null)
                .build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<MessageResponse<Void>> verifyOtp(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String otp = request.get("otp");
        String formattedPhone = FormatPhoneNumber.formatPhoneNumberTo84(phone);
        boolean isValid = twilioService.verifyOtp(formattedPhone, otp);

        if (isValid) {
            return ResponseEntity.ok(
                    MessageResponse.<Void>builder()
                            .message("Xác thực mã OTP thành công!")
                            .data(null)
                            .success(true)
                            .build()
            );
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            MessageResponse.<Void>builder()
                                    .message("Mã OTP không hợp lệ hoặc đã hết hạn.")
                                    .data(null)
                                    .success(false)
                                    .build()
                    );
        }
    }
}
