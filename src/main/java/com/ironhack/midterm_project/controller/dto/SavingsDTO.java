package com.ironhack.midterm_project.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm_project.classes.Money;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.OptionalLong;

public class SavingsDTO {
    private Money balance;

    @NotNull(message = "Primary owner can not be null")
    private Long primaryOwnerId;

    private Long secondaryOwnerId;

    @NotNull(message = "Secret key can not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secretKey;

    @DecimalMin(value = "100", message = "The minimum balance can not be lower than 100")
    private Money minimumBalance; //default 1000, min 100 when instantiated

    @DecimalMax(value = "0.5", message = "The interest rate can not be lower than 0.5")
    private BigDecimal interestRate; //Default 0.0025, max 0.5

    //CONSTRUCTORS
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

    public Optional<Money> getMinimumBalance() {
        return Optional.ofNullable(minimumBalance);
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Optional<BigDecimal> getInterestRate() {
        return Optional.ofNullable(interestRate);
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
