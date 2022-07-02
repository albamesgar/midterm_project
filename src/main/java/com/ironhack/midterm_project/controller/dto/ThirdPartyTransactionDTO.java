package com.ironhack.midterm_project.controller.dto;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.enums.TransactionType;

public class ThirdPartyTransactionDTO {
    Money amount;
    Long accountId;
    String accountSecretKey;

    public ThirdPartyTransactionDTO() {
    }

    public ThirdPartyTransactionDTO(Money amount, Long accountId, String accountSecretKey) {
        this.amount = amount;
        this.accountId = accountId;
        this.accountSecretKey = accountSecretKey;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountSecretKey() {
        return accountSecretKey;
    }

    public void setAccountSecretKey(String accountSecretKey) {
        this.accountSecretKey = accountSecretKey;
    }
}
