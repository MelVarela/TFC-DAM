package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.UserEntity;
import com.melvarela.spring_mazmorras.rest.dtos.UserDto;

public class UserDtoMapper {
    public static UserDto userEntityToUserDto(UserEntity user){
        return new UserDto(
            user.getEmail(),
            user.getName(),
            "",
            user.getProfilePicture()
        );
    }

    public static UserEntity userDtoToUserEntity(UserDto user){
        return new UserEntity(
            user.getEmail(),
            user.getName(),
            user.getPassword(),
            user.getProfilePicture()
        );
    }
}
