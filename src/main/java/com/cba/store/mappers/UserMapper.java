package com.cba.store.mappers;

import com.cba.store.dtos.RegisterUserRequest;
import com.cba.store.dtos.UpdateUserRequest;
import com.cba.store.dtos.UserDto;
import com.cba.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
