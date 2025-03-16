/*
 * @ (#) FriendServiceImpl.java       1.0     14/02/2025
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.FriendResponse;
import vn.edu.iuh.fit.olachatbackend.entities.Friend;
import vn.edu.iuh.fit.olachatbackend.entities.User;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.repositories.FriendRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.UserRepository;
import vn.edu.iuh.fit.olachatbackend.services.FriendService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public Friend addFriend(Friend friend) {
        return friendRepository.save(friend);
    }

    @Override
    public List<FriendResponse> getMyFriends() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng này"));

        String userId = user.getId();
        List<Friend> friends = friendRepository.findByUser_IdOrFriend_Id(userId, userId);

        return friends.stream().map(friend -> {
            User friendUser = friend.getUser().getId().equals(userId) ? friend.getFriend() : friend.getUser();
            return new FriendResponse(friendUser.getId(), friendUser.getDisplayName(), friendUser.getAvatar());
        }).collect(Collectors.toList());
    }
}
