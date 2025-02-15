package vn.edu.iuh.fit.olachatbackend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.edu.iuh.fit.olachatbackend.dtos.responses.UserResponse;
import vn.edu.iuh.fit.olachatbackend.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "role")
    UserResponse toUserResponse(User user);
}
