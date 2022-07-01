package com.ironhack.midterm_project.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm_project.classes.Money;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

public class SavingsDTO {
    private Money balance;

    @NotNull(message = "Primary owner can not be null")
    private Long primaryOwnerId;

    private Long secondaryOwnerId;

    @NotNull(message = "Secret key can not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secretKey;

    private Money minimumBalance = new Money(new BigDecimal(1000)); //default 1000, min 100 when instantiated

    private BigDecimal interestRate = new BigDecimal("0.0025"); //Default 0.0025, max 0.5

    //CONSTRUCTORS
    public SavingsDTO() {
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, Money minimumBalance, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, Money minimumBalance) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, String secretKey, Money minimumBalance, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, String secretKey, Money minimumBalance) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, String secretKey, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, String secretKey) {
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

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
