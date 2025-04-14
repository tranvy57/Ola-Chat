package vn.edu.iuh.fit.olachatbackend.services;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service("vonage")
@RequiredArgsConstructor
public class VonageService implements OtpService {

    @Value("${vonage.api-key}")
    private String apiKey;

    @Value("${vonage.api-secret}")
    private String apiSecret;

    @Value("${vonage.from}")
    private String from;

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private VonageClient vonageClient;

    @PostConstruct
    public void initVonage() {
        this.vonageClient = VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }

    @Override
    public void sendOtp(String phoneNumber) {
        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusSeconds(300);
        otpStorage.put(phoneNumber, new OtpData(otp, expiresAt));

        String message = "OlaChat - Mã OTP của bạn: " + otp + " (hết hạn sau 5 phút)";
        TextMessage sms = new TextMessage(from, phoneNumber, message);

        try {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(sms);
            System.out.println("Vonage status: " + response.getMessages().get(0).getStatus());
            System.out.println("Vonage error text: " + response.getMessages().get(0).getErrorText());

            var result = response.getMessages().get(0);

            if (result.getStatus() != MessageStatus.OK) {
                System.err.println("❌ Gửi OTP thất bại: " + result.getErrorText());
            } else {
                System.out.println("📤 Đã gửi OTP đến " + phoneNumber + ": " + otp);
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi OTP: " + e.getMessage());
        }
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
