package vn.edu.iuh.fit.olachatbackend.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    // Lưu OTP theo số điện thoại: <phoneNumber, OtpData>
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void sendOtp(String phoneNumber) {
        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusSeconds(300); // 5 phút
        otpStorage.put(phoneNumber, new OtpData(otp, expiresAt));

        Message.creator(
                new com.twilio.type.PhoneNumber(phoneNumber),
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                "OlaChat - Mã OTP của bạn: " + otp + " (hết hạn sau 5 phút)"
        ).create();
    }

    public boolean verifyOtp(String phoneNumber, String otp) {
        OtpData otpData = otpStorage.get(phoneNumber);
        if (otpData == null) return false;

        if (Instant.now().isAfter(otpData.expiresAt())) {
            otpStorage.remove(phoneNumber); // OTP hết hạn thì xoá
            return false;
        }

        boolean isValid = otpData.otp().equals(otp);
        if (isValid) {
            otpStorage.remove(phoneNumber); // OTP đúng thì xoá sau khi xác minh
        }
        return isValid;
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Inner class lưu thông tin OTP
    private record OtpData(String otp, Instant expiresAt) {
    }
}
