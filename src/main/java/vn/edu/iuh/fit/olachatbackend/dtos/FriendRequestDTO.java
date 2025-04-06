package vn.edu.iuh.fit.olachatbackend.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FriendRequestDTO {
    private String senderId;
    private String receiverId;


}
