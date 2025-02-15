package vn.edu.iuh.fit.olachatbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.UserResponse;
import vn.edu.iuh.fit.olachatbackend.mappers.UserMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserResponse> getUsers() {
        UserResponse userResponse = new UserResponse();
        userResponse = userMapper.toUserResponse(userRepository.findAll().get(0));
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
}
