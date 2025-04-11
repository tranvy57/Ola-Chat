package vn.edu.iuh.fit.olachatbackend.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    private String refreshToken;
}
