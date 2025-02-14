/*
 * @ (#) ConversationController.java       1.0     14/02/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.controllers;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 14/02/2025
 * @version:    1.0
 */

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.ConversationDTO;
import vn.edu.iuh.fit.olachatbackend.entities.Conversation;
import vn.edu.iuh.fit.olachatbackend.entities.Message;
import vn.edu.iuh.fit.olachatbackend.services.ConversationService;
import vn.edu.iuh.fit.olachatbackend.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final ConversationService conversationService;
    private final MessageService messageService;

    public ConversationController(ConversationService conversationService, MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Conversation> createConversation(@RequestBody ConversationDTO conversationDTO) {
        return ResponseEntity.ok(conversationService.createConversation(conversationDTO));
    }

    @GetMapping
    public ResponseEntity<List<Conversation>> getConversationsByUserId(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(conversationService.getAllConversationsByUserId(userId));
    }



    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessagesByConversationId(@PathVariable String id) {
        List<Message> messages = messageService.getMessagesByConversationId(id);
        return ResponseEntity.ok(messages);
    }
}
