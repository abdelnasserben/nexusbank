package com.nexusbank.mapper;

import com.nexusbank.dto.UserDTO;
import com.nexusbank.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toModel(UserDTO userDTO);

    UserDTO toDto(User user);
}
