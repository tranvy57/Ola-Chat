package vn.edu.iuh.fit.olachatbackend.controllers;

import lombok.RequiredArgsConstructor;
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
    public MessageResponse<Void> verifyOtp(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String otp = request.get("otp");
        String formattedPhone = FormatPhoneNumber.formatPhoneNumberTo84(phone);
        boolean isValid = twilioService.verifyOtp(formattedPhone, otp);
        return isValid
                ? MessageResponse.<Void>builder().message("Xác thực mã OTP thành công ! ").data(null).build()
                : MessageResponse.<Void>builder().message("Mã OTP Không hợp mệ ! ").data(null).build();
    }
}
