package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.dto.UserDto;
import com.JavaProject.BankTransactionService.mapper.UserMapper;
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
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users=userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user=userRepository.findById(id).orElseThrow();
        return userMapper.toDto(user);
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user=userRepository.findById(userDto.getId()).orElseThrow();
        user.setFullName(userDto.getFullName());
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
