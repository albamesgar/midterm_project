package com.ironhack.midterm_project.controller.interfaces;

import com.ironhack.midterm_project.model.users.User;

import java.util.List;

public interface UsersController {
    List<User> findUsers();
}
