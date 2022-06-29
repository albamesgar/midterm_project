package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.controller.interfaces.UsersController;
import com.ironhack.midterm_project.model.users.User;
import com.ironhack.midterm_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersControllerImpl implements UsersController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUsers() {
        return userRepository.findAll();
    }
}
