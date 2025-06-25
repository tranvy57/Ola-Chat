package vn.edu.iuh.fit.olachatbackend.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String EMAIL_UPDATE_PREFIX = "email:update:";

    public void saveWhitelistedToken(String jit, String token, long duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + jit, token, duration, timeUnit);
    }

    public boolean isTokenWhitelisted(String jit) {
        return redisTemplate.hasKey(REFRESH_TOKEN_PREFIX + jit);
    }

    public void removeWhitelistedToken(String jit) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + jit);
    }

    public void addBlacklistedToken(String jit, long duration, TimeUnit unit) {
        redisTemplate.opsForValue().set("blacklist:" + jit, "revoked", duration, unit);
    }

    public boolean isTokenBlacklisted(String jit) {
        return redisTemplate.hasKey("blacklist:" + jit);
    }


    //otp email
    private static final long OTP_EXPIRE_SECONDS = 300; // 5 phút

    public void saveOtp(String email, String otpCode) {
        redisTemplate.opsForValue().set("OTP" + email, otpCode, OTP_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }

    public String getOtp(String email) {
        String key = "OTP" + email;
        System.out.println("🔍 Đang lấy OTP với key: " + key);
        System.out.println(redisTemplate.opsForValue().get("vy"));
        return redisTemplate.opsForValue().get(key);

    }

    public void deleteOtp(String email) {
        redisTemplate.delete("OTP:" + email);
    }

    //    Giới hạn Gửi OTP mail mỗi 1 giờ 1 lầm quên mật khẩu tránh spam
    // Lưu timestamp (đơn vị milliseconds) có thời hạn
    public void setLong(String key, Long value, long duration, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value.toString(), duration, unit);
    }

    // Lấy timestamp (đã lưu dưới dạng chuỗi số)
    public Long getLong(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : null;
    }


    //UPDATE EMAIL
    // Lưu OTP và email mới tương ứng với userId
    public void saveEmailUpdateOtp(String userId, String otp, String newEmail) {
        redisTemplate.opsForHash().put(EMAIL_UPDATE_PREFIX + userId, "otp", otp);
        redisTemplate.opsForHash().put(EMAIL_UPDATE_PREFIX + userId, "newEmail", newEmail);
        redisTemplate.expire(EMAIL_UPDATE_PREFIX + userId, Duration.ofMinutes(5));
    }

    // Lấy lại OTP theo userId
    public String getEmailUpdateOtp(String userId) {
        return (String) redisTemplate.opsForHash().get(EMAIL_UPDATE_PREFIX + userId, "otp");
    }

    // Lấy lại email mới theo userId
    public String getEmailUpdateNewEmail(String userId) {
        return (String) redisTemplate.opsForHash().get(EMAIL_UPDATE_PREFIX + userId, "newEmail");
    }

    // Xoá dữ liệu OTP cập nhật email sau khi hoàn tất
    public void deleteEmailUpdateOtp(String userId) {
        redisTemplate.delete(EMAIL_UPDATE_PREFIX + userId);
    }



}

