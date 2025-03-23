package vn.edu.iuh.fit.olachatbackend.utils;

import java.util.Random;

public class OtpUtils {
    public static String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // OTP 6 số
    }
}
