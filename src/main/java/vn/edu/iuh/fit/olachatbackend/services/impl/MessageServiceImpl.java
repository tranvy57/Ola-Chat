/*
 * @ (#) MessageServiceImpl.java       1.0     14/02/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.services.impl;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 14/02/2025
 * @version:    1.0
 */
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.MessageDTO;
import vn.edu.iuh.fit.olachatbackend.entities.Message;
import vn.edu.iuh.fit.olachatbackend.repositories.MessageRepository;
import vn.edu.iuh.fit.olachatbackend.services.MessageService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public MessageDTO save(MessageDTO messageDTO) {
        Message message = Message.builder()
                .senderId(messageDTO.getSenderId())
                .conversationId(new ObjectId(messageDTO.getConversationId()))
                .content(messageDTO.getContent())
                .type(messageDTO.getType())
                .mediaUrl(messageDTO.getMediaUrl())
                .status(messageDTO.getStatus())
                .deliveryStatus(messageDTO.getDeliveryStatus())
                .readStatus(messageDTO.getReadStatus())
                .createdAt(LocalDateTime.now())
                .recalled(messageDTO.isRecalled())
                .build();
        messageRepository.save(message);
        return messageDTO;
    }

    public List<MessageDTO> getMessagesByConversationId(String conversationId) {
        List<Message> messages = messageRepository.findByConversationId(new ObjectId(conversationId));

        return messages.stream().map(msg -> MessageDTO.builder()
                .id(msg.getId().toHexString())
                .senderId(msg.getSenderId())
                .conversationId(msg.getConversationId().toHexString())
                .content(msg.getContent())
                .type(msg.getType())
                .mediaUrl(msg.getMediaUrl())
                .status(msg.getStatus())
                .deliveryStatus(msg.getDeliveryStatus())
                .readStatus(msg.getReadStatus())
                .createdAt(msg.getCreatedAt())
                .recalled(msg.isRecalled())
                .build()).toList();
    }

    public MessageDTO recallMessage(String messageId, String senderId) {
        System.out.println("Mess" + messageId);
//         Kiểm tra định dạng Message ID
        if (messageId == null || !messageId.matches("[0-9a-fA-F]{24}")) {
            throw new IllegalArgumentException("Message ID must be a valid 24-character hex string.");
        }

        // Chuyển messageId thành ObjectId
        ObjectId objectId = new ObjectId(messageId);

        // Tìm tin nhắn trong cơ sở dữ liệu
        Message message = messageRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Kiểm tra xem người gửi có quyền thu hồi tin nhắn này
        if (!message.getSenderId().equals(senderId)) {
            throw new RuntimeException("Only sender can recall this message");
        }

        // Nếu tin nhắn chưa được thu hồi thì thực hiện thu hồi
        if (!message.isRecalled()) {
            message.setRecalled(true);
            message.setContent("Tin nhắn đã được thu hồi");
            message.setMediaUrl(null);  // Nếu là tin nhắn media thì xóa URL
            messageRepository.save(message);
        }

//         Trả về MessageDTO với trạng thái tin nhắn đã thu hồi
        return MessageDTO.builder()
                .id(message.getId().toHexString())
                .senderId(message.getSenderId())
                .conversationId(message.getConversationId().toHexString())
                .content(message.getContent())
                .type(message.getType())
                .mediaUrl(null)
                .status(message.getStatus())
                .deliveryStatus(message.getDeliveryStatus())
                .readStatus(message.getReadStatus())
                .recalled(true)
                .createdAt(message.getCreatedAt())
                .build();
    }



}
