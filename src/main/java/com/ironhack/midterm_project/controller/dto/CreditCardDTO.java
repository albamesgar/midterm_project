package com.ironhack.midterm_project.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm_project.classes.Money;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

public class CreditCardDTO {
    private Money balance;

    @NotNull(message = "Primary owner can not be null")
    private Long primaryOwnerId;

    private Long secondaryOwnerId;

    @NotNull(message = "Secret key can not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secretKey;

    private Money creditLimit = new Money(new BigDecimal(100)); //default 100, max 100000 when instantiated

    private BigDecimal interestRate = new BigDecimal("0.2"); //default 0.2, min 0.1 when instantiated

    //CONSTRUCTORS

    public CreditCardDTO() {
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey,
                         Money creditLimit, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, Money creditLimit) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimit = creditLimit;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, String secretKey, Money creditLimit) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimit = creditLimit;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, String secretKey, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
    }

    //GETTERS AND SETTERS
    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Long getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(Long primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Optional<Long> getSecondaryOwnerId() {
        return Optional.ofNullable(secondaryOwnerId);
    }

    public void setSecondaryOwnerId(Long secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
