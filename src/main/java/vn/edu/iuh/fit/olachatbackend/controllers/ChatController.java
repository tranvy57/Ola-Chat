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

    // Chat room
    @MessageMapping("/message")
    public MessageResponseDTO receivePublicMessage(@Payload MessageRequest messageDTO) {
        System.out.println("Message from client for chat room: "+ messageDTO);
        messageService.save(messageDTO);

        template.convertAndSend("/chatroom/" + messageDTO.getConversationId(), messageDTO);
        UserResponse sender = userServiceImpl.getUserById(messageDTO.getSenderId());
        notificationService.notifyConversation(messageDTO.getConversationId(), messageDTO.getSenderId(), "Tin nhắn mới", "Bạn có tin nhắn từ " + sender.getDisplayName(), NotificationType.MESSAGE);
        return messageMapper.toResponseDTO(messageDTO);
    }

    // Private chat
    @MessageMapping("/private-message")
    public MessageResponseDTO receivePrivateMessage(@Payload MessageRequest messageDTO) {
        System.out.println("Message from client: "+ messageDTO);
        messageService.save(messageDTO);

        template.convertAndSend("/user/" + messageDTO.getConversationId() + "/private", messageDTO);
        UserResponse sender = userServiceImpl.getUserById(messageDTO.getSenderId());
        notificationService.notifyConversation(messageDTO.getConversationId(), messageDTO.getSenderId(), "Tin nhắn mới", "Bạn có tin nhắn từ " + sender.getDisplayName(), NotificationType.MESSAGE);
        return messageMapper.toResponseDTO(messageDTO);
    }

    @MessageMapping("/recall-message")
    public void recallMessage(@Payload MessageRequest messageDTO) {
        System.out.println("Message Recall from client: "+ messageDTO);
        MessageRequest recalled = messageService.recallMessage(messageDTO.getId(), messageDTO.getSenderId());
        template.convertAndSend("/user/" + recalled.getConversationId() + "/private", recalled);
    }


}
