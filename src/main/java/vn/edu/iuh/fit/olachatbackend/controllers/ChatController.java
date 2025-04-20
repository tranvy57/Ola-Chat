/*
 * @ (#) ChatController.java       1.0     24/01/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.controllers;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 24/01/2025
 * @version:    1.0
 */

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import vn.edu.iuh.fit.olachatbackend.dtos.MessageResponseDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.MessageRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.NotificationRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.UserResponse;
import vn.edu.iuh.fit.olachatbackend.entities.Conversation;
import vn.edu.iuh.fit.olachatbackend.entities.DeviceToken;
import vn.edu.iuh.fit.olachatbackend.entities.Participant;
import vn.edu.iuh.fit.olachatbackend.enums.NotificationType;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.mappers.MessageMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.ConversationRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.DeviceTokenRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.ParticipantRepository;
import vn.edu.iuh.fit.olachatbackend.services.MessageService;
import vn.edu.iuh.fit.olachatbackend.services.NotificationService;
import vn.edu.iuh.fit.olachatbackend.services.impl.UserServiceImpl;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final ConversationRepository conversationRepository;
    private final UserServiceImpl userServiceImpl;
    private final ParticipantRepository participantRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final MessageMapper messageMapper;

    // Public chat
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public MessageRequest receivePublicMessage(@Payload MessageRequest messageDTO) {
        return messageDTO;
    }

    // Private chat
    @MessageMapping("/private-message")
    public MessageResponseDTO receivePrivateMessage(@Payload MessageRequest messageDTO) {
        System.out.println("Message from client: "+ messageDTO);
        messageService.save(messageDTO);

        template.convertAndSend("/user/" + messageDTO.getConversationId() + "/private", messageDTO);
        notifyRecipients(messageDTO);
        return messageMapper.toResponseDTO(messageDTO);
    }

    @MessageMapping("/recall-message")
    public void recallMessage(@Payload MessageRequest messageDTO) {
        System.out.println("Message Recall from client: "+ messageDTO);
        MessageRequest recalled = messageService.recallMessage(messageDTO.getId(), messageDTO.getSenderId());
        template.convertAndSend("/user/" + recalled.getConversationId() + "/private", recalled);
    }

    private void notifyRecipients(MessageRequest messageDTO) {
        // Tìm conversation
        Conversation conversation = conversationRepository.findById(new ObjectId(messageDTO.getConversationId()))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cuộc trò chuyện"));

        // Tìm sender (để lấy displayName)
        UserResponse sender = userServiceImpl.getUserById(messageDTO.getSenderId());

        // Lọc ra danh sách người nhận (tất cả trừ sender)
        List<String> receiverIds = participantRepository.findParticipantByConversationId(conversation.getId()).stream()
                .map(Participant::getUserId) // lấy userId từ mỗi Participant
                .filter(userId -> !userId.equals(messageDTO.getSenderId())) // bỏ sender ra
                .toList();

        for (String receiverId : receiverIds) {
            DeviceToken deviceToken = deviceTokenRepository.findByUserId(receiverId);
            if (deviceToken != null) {
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .title("Tin nhắn mới")
                        .body("Bạn có tin nhắn từ " + sender.getDisplayName())
                        .token(deviceToken.getToken())
                        .type(NotificationType.MESSAGE)
                        .senderId(sender.getUserId())
                        .receiverId(receiverId)
                        .build();

                notificationService.sendNotification(notificationRequest);
            } else {
                System.out.println("Không tìm thấy token cho user: " + receiverId);
            }
        }
    }


}
