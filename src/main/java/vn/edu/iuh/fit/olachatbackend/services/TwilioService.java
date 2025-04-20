package vn.edu.iuh.fit.olachatbackend.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service("twilio")
@RequiredArgsConstructor
public class TwilioService implements OtpService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public void sendOtp(String phoneNumber) {
        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusSeconds(300);
        otpStorage.put(phoneNumber, new OtpData(otp, expiresAt));

        Message.creator(
                new com.twilio.type.PhoneNumber(phoneNumber),
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                "OlaChat - Mã OTP của bạn: " + otp + " (hết hạn sau 5 phút)"
        ).create();
    }

    @Override
    public boolean verifyOtp(String phoneNumber, String otp) {
        OtpData otpData = otpStorage.get(phoneNumber);
        if (otpData == null || Instant.now().isAfter(otpData.expiresAt())) {
            otpStorage.remove(phoneNumber);
            return false;
        }

        boolean isValid = otpData.otp().equals(otp);
        if (isValid) otpStorage.remove(phoneNumber);
        return isValid;
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private record OtpData(String otp, Instant expiresAt) {}
}
