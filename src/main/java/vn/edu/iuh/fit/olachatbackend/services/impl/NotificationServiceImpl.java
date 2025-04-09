/*
 * @ (#) NotificationServiceImpl.java       1.0     06/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.services.impl;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 06/04/2025
 * @version:    1.0
 */

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public NotificationServiceImpl(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void sendFriendRequestNotification(String senderId, String receiverId, String receiverToken) {
        String body = "Bạn có lời mời kết bạn từ người dùng " + senderId;
        Message message = Message.builder()
                .setToken(receiverToken)
                .setNotification(Notification.builder()
                        .setTitle("Lời mời kết bạn")
                        .setBody(body)
                        .build())
                .putData("senderId", senderId)
                .putData("receiverId", receiverId)
                .build();
        try {
            firebaseMessaging.send(message);
            System.out.println("Đã gửi thông báo đến " + receiverId);
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi thông báo: " + e.getMessage());
        }
    }
}
