package com.JavaProject.BankTransactionService.API;

import com.JavaProject.BankTransactionService.dto.UserDto;
import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserAPI {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userServiceImpl.getAllUsers();
    }

    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable(value = "id") Long id){
        return userServiceImpl.getUserById(id);
    }

    @PostMapping
    public void addUser(@RequestBody User user){
        userServiceImpl.addUser(user);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto){
        return userServiceImpl.updateUser(userDto);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable(value = "id") Long id){
        userServiceImpl.deleteUser(id);
    }
}
