package com.ironhack.midterm_project.controller.dto;

import com.ironhack.midterm_project.classes.Money;

public class BalanceDTO {
    private Money newBalance;

    public Money getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Money newBalance) {
        this.newBalance = newBalance;
    }
}
