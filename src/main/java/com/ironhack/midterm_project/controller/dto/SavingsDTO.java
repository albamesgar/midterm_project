package com.ironhack.midterm_project.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm_project.classes.Money;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

public class SavingsDTO {
    @NotNull(message = "Initial balance can not be null")
    private Money balance;

    @NotNull(message = "Primary owner can not be null")
    private Long primaryOwnerId;

    private Long secondaryOwnerId=0L;

    @NotNull(message = "Secret key can not be null")
    private String secretKey;

//    private MinimumBalanceDTO minimumBalance = new MinimumBalanceDTO(); //default 1000, min 100 when instantiated
    @DecimalMin(value = "100", message = "The minimum balance can not be lower than 100")
    private BigDecimal minimumBalanceAmount = BigDecimal.valueOf(1000);
    private Currency minimumBalanceCurrency = Currency.getInstance("USD");

    @DecimalMax(value = "0.5", message = "The interest rate can not be lower than 0.5")
    private BigDecimal interestRate = new BigDecimal("0.0025"); //Default 0.0025, max 0.5

    //CONSTRUCTORS
    public SavingsDTO() {
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, BigDecimal minimumBalanceAmount,
                      Currency minimumBalanceCurrency,String secretKey,BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalanceAmount = minimumBalanceAmount;
        this.minimumBalanceCurrency = minimumBalanceCurrency;
        this.interestRate = interestRate;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, BigDecimal minimumBalanceAmount,
                      String secretKey,BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalanceAmount = minimumBalanceAmount;
        this.interestRate = interestRate;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, BigDecimal minimumBalanceAmount,
                      Currency minimumBalanceCurrency, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalanceAmount = minimumBalanceAmount;
        this.minimumBalanceCurrency = minimumBalanceCurrency;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, BigDecimal minimumBalanceAmount,
                      String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalanceAmount = minimumBalanceAmount;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey,
                      BigDecimal interestRate) {
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

    public SavingsDTO(Money balance, Long primaryOwnerId, BigDecimal minimumBalanceAmount,
                      Currency minimumBalanceCurrency, String secretKey, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalanceAmount = minimumBalanceAmount;
        this.minimumBalanceCurrency = minimumBalanceCurrency;
        this.interestRate = interestRate;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, BigDecimal minimumBalanceAmount,
                      String secretKey, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalanceAmount = minimumBalanceAmount;
        this.interestRate = interestRate;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, BigDecimal minimumBalanceAmount,
                      Currency minimumBalanceCurrency,String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalanceAmount = minimumBalanceAmount;
        this.minimumBalanceCurrency = minimumBalanceCurrency;
    }

    public SavingsDTO(Money balance, Long primaryOwnerId, BigDecimal minimumBalanceAmount,String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.minimumBalanceAmount = minimumBalanceAmount;
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

    public Long getSecondaryOwnerId() {
        return secondaryOwnerId;
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

    public BigDecimal getMinimumBalanceAmount() {
        return minimumBalanceAmount;
    }

    public void setMinimumBalanceAmount(BigDecimal minimumBalanceAmount) {
        this.minimumBalanceAmount = minimumBalanceAmount;
    }

    public Currency getMinimumBalanceCurrency() {
        return minimumBalanceCurrency;
    }

    public void setMinimumBalanceCurrency(Currency minimumBalanceCurrency) {
        this.minimumBalanceCurrency = minimumBalanceCurrency;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
