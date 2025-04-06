package vn.edu.iuh.fit.olachatbackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestResponse {
    private String requestId;
    private String userId;
    private String displayName;
    private String avatar;
}
