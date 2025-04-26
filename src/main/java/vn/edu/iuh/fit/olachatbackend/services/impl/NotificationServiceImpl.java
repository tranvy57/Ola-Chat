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
import org.bson.types.ObjectId;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.NotificationDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.NotificationPageDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.NotificationRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.UserResponse;
import vn.edu.iuh.fit.olachatbackend.entities.Conversation;
import vn.edu.iuh.fit.olachatbackend.entities.DeviceToken;
import vn.edu.iuh.fit.olachatbackend.entities.Notification;
import vn.edu.iuh.fit.olachatbackend.entities.Participant;
import vn.edu.iuh.fit.olachatbackend.enums.NotificationType;
import vn.edu.iuh.fit.olachatbackend.exceptions.InternalServerErrorException;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.mappers.NotificationMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.ConversationRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.DeviceTokenRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.NotificationRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.ParticipantRepository;
import vn.edu.iuh.fit.olachatbackend.services.NotificationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final DeviceTokenRepository deviceTokenRepository;
    private final ConversationRepository conversationRepository;
    private final UserServiceImpl userServiceImpl;
    private final ParticipantRepository participantRepository;

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
                    .read(false)
                    .type(request.getType())
                    .createdAt(LocalDateTime.now())
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
    public NotificationPageDTO getNotificationsByUser(String userId, Pageable pageable) {
        Page<NotificationDTO> page = notificationRepository.findByReceiverId(userId, pageable)
                .map(notificationMapper::toDTO);

        // Tạo đối tượng NotificationPageDTO để trả về
        return NotificationPageDTO.builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresentOrElse(
                notification -> {
                    // Đánh dấu thông báo là đã đọc
                    notification.setRead(true);
                    notificationRepository.save(notification);
                },
                () -> {
                    // Thông báo lỗi nếu không tìm thấy thông báo
                    throw new NotFoundException("Thông báo không tồn tại với ID: " + notificationId);
                }
        );
    }

    @Override
    public void notifyConversation(String conversationId, String senderId, String title, String body, NotificationType type) {
        Conversation conversation = conversationRepository.findById(new ObjectId(conversationId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cuộc trò chuyện"));

        UserResponse sender = userServiceImpl.getUserById(senderId);

        List<Participant> participants = participantRepository.findParticipantByConversationId(new ObjectId(conversationId));

        for (Participant participant : participants) {
            String receiverId = participant.getUserId();

            if (receiverId.equals(senderId)) continue;
            if (participant.isMuted()) continue;

            DeviceToken deviceToken = deviceTokenRepository.findByUserId(receiverId);
            if (deviceToken == null) continue;

            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .title(title)
                    .body(body)
                    .token(deviceToken.getToken())
                    .type(type)
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .build();

            sendNotification(notificationRequest);
        }
    }

}
