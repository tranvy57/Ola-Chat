package vn.edu.iuh.fit.olachatbackend.dtos.responses;

import com.google.firebase.remoteconfig.internal.TemplateResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String accessToken;
    String refreshToken;
    boolean authenticated;
    UserResponse user;
}
