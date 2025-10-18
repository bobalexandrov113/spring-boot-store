package com.cba.store.mappers;

import com.cba.store.dtos.UserDto;
import com.cba.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
}
