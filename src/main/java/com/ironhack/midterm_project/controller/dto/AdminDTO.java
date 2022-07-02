package com.ironhack.midterm_project.controller.dto;

import javax.validation.constraints.NotNull;

public class AdminDTO {
    private String username;
    @NotNull(message = "password can not be null")
    private String password;
    @NotNull(message = "Role can not be null")
    private String role;

    public AdminDTO() {
    }

    public AdminDTO(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
