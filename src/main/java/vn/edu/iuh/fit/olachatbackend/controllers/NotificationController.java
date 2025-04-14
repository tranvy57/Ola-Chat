/*
 * @ (#) NotificationController.java       1.0     14/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.controllers;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 14/04/2025
 * @version:    1.0
 */

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.NotificationDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.MessageResponse;
import vn.edu.iuh.fit.olachatbackend.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public MessageResponse<List<NotificationDTO>> getUserNotifications(@PathVariable String userId) {
        List<NotificationDTO> data = notificationService.getNotificationsByUser(userId);
        return MessageResponse.<List<NotificationDTO>>builder()
                .message("Lấy danh sách thông báo thành công!")
                .data(data)
                .build();
    }

    @PutMapping("/{id}/read")
    public MessageResponse<String> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return MessageResponse.<String>builder()
                .message("Đã gửi lời mời kết bạn.")
                .data(null)
                .build();
    }

    @PostMapping("/register-device")
    public MessageResponse<?> registerDevice(@RequestParam String userId, @RequestParam String token) {
        notificationService.registerDevice(userId, token);
        return MessageResponse.<Void>builder()
                .message("Đã đăng ký thiết bị thành công.")
                .build();

    }
}
