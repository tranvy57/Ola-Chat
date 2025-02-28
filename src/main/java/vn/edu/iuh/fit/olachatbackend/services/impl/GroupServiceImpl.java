/*
 * @ (#) GroupServiceImpl.java       1.0     28/02/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.services.impl;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 28/02/2025
 * @version:    1.0
 */

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.ConversationDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.GroupUpdateRequest;
import vn.edu.iuh.fit.olachatbackend.entities.Conversation;
import vn.edu.iuh.fit.olachatbackend.entities.LastMessage;
import vn.edu.iuh.fit.olachatbackend.entities.Message;
import vn.edu.iuh.fit.olachatbackend.entities.Participant;
import vn.edu.iuh.fit.olachatbackend.enums.ConversationType;
import vn.edu.iuh.fit.olachatbackend.enums.ParticipantRole;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.mappers.ConversationMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.ConversationRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.MessageRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.ParticipantRepository;
import vn.edu.iuh.fit.olachatbackend.services.GroupService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;
    private final ConversationMapper conversationMapper;
    private final MessageRepository messageRepository;

    @Override
    public ConversationDTO createGroup(String creatorId, String name, String avatar, List<String> userIds) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhóm không được để trống");
        }
        if (userIds == null || userIds.size() < 2) {
            throw new IllegalArgumentException("Nhóm phải có ít nhất 3 thành viên");
        }

        // Create group
        Conversation group = Conversation.builder()
                .name(name)
                .avatar(avatar) // Avatar can allow null
                .type(ConversationType.GROUP)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Conversation savedGroup = conversationRepository.save(group);

        List<Participant> participants = new ArrayList<>();

        // Assign admin to creator
        participants.add(Participant.builder()
                .conversationId(savedGroup.getId())
                .userId(creatorId)
                .role(ParticipantRole.ADMIN)
                .joinedAt(LocalDateTime.now())
                .build());

        // Add members into group
        for (String userId : userIds) {
            participants.add(Participant.builder()
                    .conversationId(savedGroup.getId())
                    .userId(userId)
                    .role(ParticipantRole.MEMBER)
                    .joinedAt(LocalDateTime.now())
                    .build());
        }

        participantRepository.saveAll(participants);

        return conversationMapper.toDTO(savedGroup);
    }

    @Override
    public ConversationDTO getGroupById(ObjectId id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nhóm không tồn tại"));

        // Get list userId from Participant
        List<String> userIds = participantRepository.findParticipantByConversationId(id)
                .stream().map(Participant::getUserId).collect(Collectors.toList());

        // Get last message
        if (conversation.getLastMessage() == null) {
            Message lastMessage = messageRepository.findTopByConversationIdOrderByCreatedAtDesc(id);
            if (lastMessage != null) {
                conversation.setLastMessage(
                        LastMessage.builder()
                                .messageId(lastMessage.getId())
                                .content(lastMessage.getContent())
                                .createdAt(lastMessage.getCreatedAt())
                                .build()
                );
                conversationRepository.save(conversation);
            }
        }

        // Convert to DTO
        ConversationDTO conversationDTO = conversationMapper.toDTO(conversation);
        conversationDTO.setUserIds(userIds);

        return conversationDTO;
    }

    @Override
    public ConversationDTO updateGroup(ObjectId groupId, String userId, GroupUpdateRequest request) {
        Conversation group = conversationMapper.toEntity(getGroupById(groupId));

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            group.setName(request.getName());
        }
        if (request.getAvatar() != null) {
            group.setAvatar(request.getAvatar());
        }
        group.setUpdatedAt(LocalDateTime.now());

        return conversationMapper.toDTO(conversationRepository.save(group));
    }
}
