package vn.edu.iuh.fit.olachatbackend.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.UserRegisterRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.MessageResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.UserResponse;
import vn.edu.iuh.fit.olachatbackend.services.UserService;

import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping
    public MessageResponse<List<UserResponse>> getAllUsers() {
        return MessageResponse.<List<UserResponse>>builder()
                .message("Lấy danh sách người dùng thành công")
                .data(userService.getUsers())
                .build();
    }

    @PostMapping
    public MessageResponse<UserResponse> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        return MessageResponse.<UserResponse>builder()
                .message("Đăng ký người dùng thành công")
                .data(userService.registerUser(request))
                .build();
    }

}
