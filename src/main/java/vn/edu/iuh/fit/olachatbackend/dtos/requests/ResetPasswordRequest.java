package vn.edu.iuh.fit.olachatbackend.dtos.requests;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
}
