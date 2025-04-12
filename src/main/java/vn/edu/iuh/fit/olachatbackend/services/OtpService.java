package vn.edu.iuh.fit.olachatbackend.services;

public interface OtpService {
    void sendOtp(String phoneNumber);
    boolean verifyOtp(String phoneNumber, String otp);
}
