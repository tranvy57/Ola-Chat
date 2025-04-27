/*
 * @ (#) PollServiceImpl.java       1.0     27/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.services.impl;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 27/04/2025
 * @version:    1.0
 */

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.AddOptionRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.CreatePollRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.VoteRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.PollOptionResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.PollResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.PollResultsResponse;
import vn.edu.iuh.fit.olachatbackend.entities.Conversation;
import vn.edu.iuh.fit.olachatbackend.entities.Poll;
import vn.edu.iuh.fit.olachatbackend.entities.PollOption;
import vn.edu.iuh.fit.olachatbackend.entities.User;
import vn.edu.iuh.fit.olachatbackend.exceptions.BadRequestException;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.mappers.PollMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.*;
import vn.edu.iuh.fit.olachatbackend.services.PollService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final VoteRepository voteRepository;
    private final PollMapper pollMapper;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public PollResponse createPoll(CreatePollRequest request) {
        // Check question
        if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
            throw new BadRequestException("Câu hỏi không được để trống");
        }

        // Check number of option
        if (request.getOptions() == null || request.getOptions().size() < 2) {
            throw new BadRequestException("Phải có ít nhất 2 tùy chọn");
        }

        // Check option
        for (String option : request.getOptions()) {
            if (option == null || option.trim().isEmpty()) {
                throw new BadRequestException("Lựa chọn không được để trống");
            }
        }

        // Check groupId and creatorId
        if (request.getGroupId() == null || request.getGroupId().isEmpty()) {
            throw new BadRequestException("Group id không được để trống");
        }

        // Check deadline
        if (request.getDeadline() != null && request.getDeadline().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Ngày hết hạn phải là thời điểm trong tương lai");
        }

        // Check group exists
        Conversation group = conversationRepository.findById(new ObjectId(request.getGroupId()))
                .orElseThrow(() -> new BadRequestException("Nhóm không tồn tại"));

        // Get user info from JWT (userId)
        User user = getCurrentUser();

        // Check user if user in group
        boolean isMember = participantRepository.existsByConversationIdAndUserId(new ObjectId(request.getGroupId()), user.getId());
        if (!isMember) {
            throw new BadRequestException("Bạn không phải là thành viên của nhóm này");
        }

        // Save poll
        Poll poll = pollMapper.toPoll(request);
        poll.setCreatorId(user.getId());
        poll = pollRepository.save(poll);

        // Save option
        for (String optionText : request.getOptions()) {
            PollOption option = new PollOption();
            option.setPollId(poll.getId());
            option.setOptionText(optionText);
            pollOptionRepository.save(option);
        }

        // Create response
        PollResponse response = pollMapper.toPollResponse(poll);
        List<PollOption> options = pollOptionRepository.findByPollId(poll.getId());
        response.setOptions(options.stream().map(pollMapper::toPollOptionResponse).collect(Collectors.toList()));
        return response;
    }

    @Override
    public PollOptionResponse addOption(String pollId, AddOptionRequest request) {
        // Check pollId
        if (pollId == null) {
            throw new IllegalArgumentException("Poll ID không được để trống");
        }

        // Check poll exists
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bình chọn"));

        // Check role
        if (!poll.isAllowAddOptions()) {
            throw new BadRequestException("Không được phép thêm tùy chọn cho bình chọn này");
        }

        // Check poll expiration
        if (poll.getDeadline() != null && poll.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Bình chọn đã hết hạn, không thể thêm tùy chọn");
        }

        // Check option
        if (request.getOptionText() == null || request.getOptionText().trim().isEmpty()) {
            throw new IllegalArgumentException("Tùy chọn không được để trống");
        }

        // Save new option
        PollOption option = pollMapper.toPollOption(request, pollId);
        option = pollOptionRepository.save(option);

        return pollMapper.toPollOptionResponse(option);
    }

    @Override
    public void vote(String pollId, VoteRequest request) {

    }

    @Override
    public PollResultsResponse getPollResults(String pollId, String userId) {
        return null;
    }

    private User getCurrentUser() {
        // Check user
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        return userRepository.findByUsername(name)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng này"));
    }


}
