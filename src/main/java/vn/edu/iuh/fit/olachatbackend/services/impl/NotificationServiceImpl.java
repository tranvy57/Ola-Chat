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
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.NotificationDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.NotificationRequest;
import vn.edu.iuh.fit.olachatbackend.entities.DeviceToken;
import vn.edu.iuh.fit.olachatbackend.entities.Notification;
import vn.edu.iuh.fit.olachatbackend.exceptions.InternalServerErrorException;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.mappers.NotificationMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.DeviceTokenRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.NotificationRepository;
import vn.edu.iuh.fit.olachatbackend.services.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final DeviceTokenRepository deviceTokenRepository;

    @Override
    public void registerDevice(String userId, String token) {
        DeviceToken deviceToken = deviceTokenRepository.findByUserId(userId);
        if (deviceToken == null) {
            deviceToken = new DeviceToken();
            deviceToken.setUserId(userId);
        }
        deviceToken.setToken(token);
        deviceTokenRepository.save(deviceToken);
    }

    @Override
    public void sendNotification(NotificationRequest request) {
        Message message = Message.builder()
                .setToken(request.getToken())
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .putData("senderId", request.getSenderId())
                .putData("receiverId", request.getReceiverId())
                .build();
        try {
            firebaseMessaging.send(message);
            System.out.println("Đã gửi thông báo đến " + request.getReceiverId());

            // Save notification
            Notification notification = Notification.builder()
                    .title(request.getTitle())
                    .body(request.getBody())
                    .senderId(request.getSenderId())
                    .receiverId(request.getReceiverId())
                    .isRead(false)
                    .type(request.getType())
                    .build();
            notificationRepository.save(notification);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new InternalServerErrorException("Lỗi khi truy xuất dữ liệu");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi gửi thông báo");
        }
    }

    @Override
    public List<NotificationDTO> getNotificationsByUser(String userId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream().map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
