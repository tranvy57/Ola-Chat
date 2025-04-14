package vn.edu.iuh.fit.olachatbackend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.edu.iuh.fit.olachatbackend.dtos.requests.UserRegisterRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.UserUpdateInfoRequest;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.UserResponse;
import vn.edu.iuh.fit.olachatbackend.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "role")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "nickname", source = "nickname")
    UserResponse toUserResponse(User user);

    @Mapping(target = "role", source = "role")
    User toUser(UserRegisterRequest user);

    User toUser(UserUpdateInfoRequest user);

    List<UserResponse> toUserResponseList(List<User> users);
}
