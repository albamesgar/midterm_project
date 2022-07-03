package com.ironhack.midterm_project.controller.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm_project.classes.Address;
import com.ironhack.midterm_project.model.users.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

public class AccountHolderDTO {
    @NotNull(message = "username can not be null")
    private String username;
    @NotNull(message = "password can not be null")
    private String password;
    @NotNull(message = "Role can not be null")
    private String role;
    @NotNull(message = "The date of birth can not be null")
    private Date dateOfBirth;
    @NotNull(message = "Primary Address can not be null")
    private Address primaryAddress;
    private Address mailingAddress = null;

    //CONSTRUCTORS
    public AccountHolderDTO() {
    }

    public AccountHolderDTO(String username, String password, String role, Date dateOfBirth, Address primaryAddress) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }

    public AccountHolderDTO(String username, String password, String role, Date dateOfBirth, Address primaryAddress,
                            Address mailingAddress) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    //GETTERS AND SETTERS
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
