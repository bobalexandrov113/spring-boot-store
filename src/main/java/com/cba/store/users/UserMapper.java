package com.cba.store.users;

import com.cba.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
