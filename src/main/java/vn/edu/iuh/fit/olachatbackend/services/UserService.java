package vn.edu.iuh.fit.olachatbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.UserRegisterRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.UserResponse;
import vn.edu.iuh.fit.olachatbackend.entities.User;
import vn.edu.iuh.fit.olachatbackend.exceptions.ErrorMessageDto;
import vn.edu.iuh.fit.olachatbackend.exceptions.InternalServerErrorException;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.mappers.UserMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
