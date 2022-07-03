package com.ironhack.midterm_project.controller.dto.accounts;

import com.ironhack.midterm_project.classes.Money;

public class BalanceDTO {
    private Money newBalance;

    //CONSTRUCTORS
    public BalanceDTO() {
    }

    public BalanceDTO(Money newBalance) {
        this.newBalance = newBalance;
    }

    //GETTERS AND SETTERS
    public Money getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Money newBalance) {
        this.newBalance = newBalance;
    }
}
