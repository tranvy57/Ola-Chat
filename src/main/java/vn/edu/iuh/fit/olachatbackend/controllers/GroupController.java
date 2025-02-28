/*
 * @ (#) GroupController.java       1.0     28/02/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.controllers;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 28/02/2025
 * @version:    1.0
 */

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.ConversationDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.GroupRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.GroupUpdateRequest;
import vn.edu.iuh.fit.olachatbackend.services.GroupService;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest request) {
        try {
            ConversationDTO group = groupService.createGroup(request.getCreatorId(), request.getName(), request.getAvatar(), request.getUserIds());
            return ResponseEntity.ok(group);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@PathVariable String id) {
        return ResponseEntity.ok(groupService.getGroupById(new ObjectId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable String id, @RequestParam String userId,
                                                    @RequestBody GroupUpdateRequest request) {
        return ResponseEntity.ok(groupService.updateGroup(new ObjectId(id), userId, request));
    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteGroup(@PathVariable ObjectId id, @RequestParam String userId) {
//        groupService.deleteGroup(id, userId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/{id}/join")
//    public ResponseEntity<Void> joinGroup(@PathVariable ObjectId id, @RequestParam String userId) {
//        groupService.joinGroup(id, userId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/{id}/leave")
//    public ResponseEntity<Void> leaveGroup(@PathVariable ObjectId id, @RequestParam String userId) {
//        groupService.leaveGroup(id, userId);
//        return ResponseEntity.ok().build();
//    }

}
