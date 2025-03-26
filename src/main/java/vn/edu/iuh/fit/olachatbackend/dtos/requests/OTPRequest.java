package vn.edu.iuh.fit.olachatbackend.dtos.requests;

import lombok.Data;

@Data
public class OTPRequest {
    private String email;
    private String otp;
}
