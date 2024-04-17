package com.JavaProject.BankTransactionService.mapper;


import com.JavaProject.BankTransactionService.dto.UserDto;
import com.JavaProject.BankTransactionService.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);

    List<User> toEntityList(List<UserDto> userDtos);
}
