package vn.edu.iuh.fit.olachatbackend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.edu.iuh.fit.olachatbackend.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    @JsonProperty("userId")
    String userId;
    String email;
    String phone;
    String username;
    String fullName;
    Role role;


}
