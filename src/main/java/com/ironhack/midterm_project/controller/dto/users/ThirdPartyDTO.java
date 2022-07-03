package com.ironhack.midterm_project.controller.dto.users;

import javax.validation.constraints.NotNull;

public class ThirdPartyDTO {
    @NotNull(message = "Hashed key can not be null")
    private String hashedKey;
    @NotNull(message = "Name can not be null")
    private String name;

    //CONSTRUCTORS
    public ThirdPartyDTO() {
    }

    public ThirdPartyDTO(String hashedKey, String name) {
        this.hashedKey = hashedKey;
        this.name = name;
    }

    //GETTERS AND SETTERS
    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
