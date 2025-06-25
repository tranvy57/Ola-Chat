package vn.edu.iuh.fit.olachatbackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendResponse {
    private String userId;
    private String displayName;
    private String avatar;
}
