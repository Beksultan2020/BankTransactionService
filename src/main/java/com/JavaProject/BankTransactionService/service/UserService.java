package com.JavaProject.BankTransactionService.service;

import com.JavaProject.BankTransactionService.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

    void addUser(User user);

    User updateUser(User userDto);

    void deleteUser(Long id);
}
