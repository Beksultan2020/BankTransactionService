package com.JavaProject.BankTransactionService.service.serviceImpl;


import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.repository.UserRepository;
import com.JavaProject.BankTransactionService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();

    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User updatedUser=userRepository.findById(user.getId()).orElseThrow();
        updatedUser.setFullName(user.getFullName());
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
