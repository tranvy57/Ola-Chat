/*
 * @ (#) UserServiceImpl.java       1.0     14/02/2025
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.IntrospectRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.UserRegisterRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.UserUpdateInfoRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.IntrospectResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.UserResponse;
import vn.edu.iuh.fit.olachatbackend.entities.Participant;
import vn.edu.iuh.fit.olachatbackend.entities.User;
import vn.edu.iuh.fit.olachatbackend.exceptions.InternalServerErrorException;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.exceptions.UnauthorizedException;
import vn.edu.iuh.fit.olachatbackend.mappers.UserMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.ParticipantRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.UserRepository;
import vn.edu.iuh.fit.olachatbackend.services.AuthenticationService;
import vn.edu.iuh.fit.olachatbackend.services.RedisService;
import vn.edu.iuh.fit.olachatbackend.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ParticipantRepository participantRepository;
    private final AuthenticationService authenticationService;
    private final RedisService redisService;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserResponse getUserById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với id: " + id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<UserResponse> getUsers() {
        UserResponse userResponse = new UserResponse();
        userResponse = userMapper.toUserResponse(userRepository.findAll().get(0));
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse registerUser(UserRegisterRequest request){
        String username = request.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new InternalServerErrorException("Tên đăng nhập đã tồn tại");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }


    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng này"));

        return userMapper.toUserResponse(user);
    }


    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserResponse> getUsersByConversationId(String conversationId) {
        List<Participant> participants = participantRepository.findParticipantByConversationId(new ObjectId(conversationId));

        List<String> userIds = participants.stream()
                .map(Participant::getUserId)
                .toList();

        List<User> users = userRepository.findAllById(userIds);

        return users.stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse getMyInfo(String token)  {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token không hợp lệ!");
        }

        try {
            token = token.substring(7);

            IntrospectResponse response = authenticationService.introspect(new IntrospectRequest(token));

            if (!response.isValid()) {
                throw new UnauthorizedException("Token không hợp lệ hoặc đã hết hạn!");
            }

            String email = response.getUserId(); // Kiểm tra lại nếu email có trong response

            return userRepository.findByEmail(email)
                    .map(userMapper::toUserResponse)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với email: " + email));

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException("Lỗi xử lý xác thực token: " + e.getMessage());
        }
    }

    @Override
    public UserResponse updateMyInfo(UserUpdateInfoRequest request) {
        var context = SecurityContextHolder.getContext();
        String currentUsername = context.getAuthentication().getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng này"));

        User updateUser = userMapper.toUser(request);

        User updatedUser = userRepository.save(updateUser);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse changePassword(String oldPassword, String newPassword) {
        var context = SecurityContextHolder.getContext();
        String currentUsername = context.getAuthentication().getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng này"));

        // Giới hạn 1 giờ/lần đổi mật khẩu
        String redisKey = "PASSWORD_CHANGE_LIMIT:" + user.getId();
        Long lastChanged = redisService.getLong(redisKey);
        long now = System.currentTimeMillis();

        if (lastChanged != null && (now - lastChanged) < 3600_000) {
            throw new UnauthorizedException("Bạn chỉ có thể đổi mật khẩu mỗi 1 giờ. Vui lòng thử lại sau.");
        }

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Mật khẩu cũ không chính xác");
        }

        // Đổi mật khẩu và cập nhật
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        // Cập nhật mốc thời gian đổi mật khẩu gần nhất vào Redis
        redisService.setLong(redisKey, now, 1, TimeUnit.HOURS);

        return userMapper.toUserResponse(updatedUser);
    }
  
    @Override
    public UserResponse searchUserByPhoneOrEmail(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query không được để trống");
        }

        Optional<User> user;
        if (query.contains("@")) {
            user = userRepository.findByEmail(query);
        } else {
            user = userRepository.findByUsername(query);
        }

        if(user.isEmpty()) {
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        return userMapper.toUserResponse(user.get());
    }

}
