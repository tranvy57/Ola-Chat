package vn.edu.iuh.fit.olachatbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.FriendRequestDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.FriendRequestResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.MessageResponse;
import vn.edu.iuh.fit.olachatbackend.services.FriendRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/send-request")
    public MessageResponse<FriendRequestDTO> sendFriendRequest(@RequestBody FriendRequestDTO request) {
        FriendRequestDTO data = friendRequestService.sendFriendRequest(request);
        return MessageResponse.<FriendRequestDTO>builder()
                .message("Đã gửi lời mời kết bạn.")
                .data(data)
                .build();
    }

    @GetMapping("/requests/received")
    public MessageResponse<List<FriendRequestResponse>> getReceivedFriendRequests() {
        return MessageResponse.<List<FriendRequestResponse>>builder()
                .message("Danh sách lời mời kết bạn đã nhận.")
                .data(friendRequestService.getReceivedFriendRequests())
                .build();
    }
}
