package com.JavaProject.BankTransactionService.service;

import com.JavaProject.BankTransactionService.dto.UserDto;
import com.JavaProject.BankTransactionService.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void addUser(User user);

    UserDto updateUser(UserDto userDto);

    void deleteUser(Long id);
}
