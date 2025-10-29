package com.xavier.picpaysimplificado.controllers;

import com.xavier.picpaysimplificado.domain.user.User;
import com.xavier.picpaysimplificado.dto.UserDto;
import com.xavier.picpaysimplificado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody  UserDto user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return this.userService.getAllUsers();
    }
}
