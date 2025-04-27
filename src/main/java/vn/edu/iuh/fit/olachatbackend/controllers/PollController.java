/*
 * @ (#) PollController.java       1.0     27/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.controllers;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 27/04/2025
 * @version:    1.0
 */

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.AddOptionRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.CreatePollRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.MessageResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.PollOptionResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.PollResponse;
import vn.edu.iuh.fit.olachatbackend.services.PollService;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
class PollController {
    private final PollService pollService;

    @PostMapping
    public ResponseEntity<MessageResponse<PollResponse>> createPoll(@RequestBody CreatePollRequest request) {
        PollResponse createdPoll = pollService.createPoll(request);
        MessageResponse<PollResponse> response = new MessageResponse<>(201, "Tạo bình chọn thành công", true, createdPoll);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{pollId}/options")
    public ResponseEntity<MessageResponse<PollOptionResponse>> addOption(@PathVariable String pollId,
                                                                         @RequestBody AddOptionRequest request) {
        PollOptionResponse addedOption = pollService.addOption(pollId, request);
        MessageResponse<PollOptionResponse> response = new MessageResponse<>(201, "Thêm lựa chọn thành công", true, addedOption);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}