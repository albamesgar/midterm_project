package com.ironhack.midterm_project.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm_project.classes.Money;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

public class CreditCardDTO {
    @NotNull(message = "Initial balance can not be null")
    private Money balance;

    @NotNull(message = "Primary owner can not be null")
    private Long primaryOwnerId;

    private Long secondaryOwnerId=0L;

    @NotNull(message = "Secret key can not be null")
    private String secretKey;

    @DecimalMax(value = "100000", message = "The credit limit can not be higher than 100000")
    private BigDecimal creditLimitAmount = BigDecimal.valueOf(100);
    private Currency creditLimitCurrency = Currency.getInstance("USD");
    @DecimalMin(value = "0.1", message = "The interest rate can not be lower than 0.1")
    private BigDecimal interestRate = new BigDecimal("0.2"); //default 0.2, min 0.1 when instantiated

    //CONSTRUCTORS

    public CreditCardDTO() {
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, BigDecimal creditLimitAmount,
                         Currency creditLimitCurrency, String secretKey,BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimitAmount = creditLimitAmount;
        this.creditLimitCurrency = creditLimitCurrency;
        this.interestRate = interestRate;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, BigDecimal creditLimitAmount,
                         String secretKey,BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimitAmount = creditLimitAmount;
        this.interestRate = interestRate;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, BigDecimal creditLimitAmount,
                         Currency creditLimitCurrency, String secretKey,BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimitAmount = creditLimitAmount;
        this.creditLimitCurrency = creditLimitCurrency;
        this.interestRate = interestRate;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId,BigDecimal creditLimitAmount,
                         String secretKey,BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimitAmount = creditLimitAmount;
        this.interestRate = interestRate;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId,BigDecimal creditLimitAmount,
                         Currency creditLimitCurrency, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimitAmount = creditLimitAmount;
        this.creditLimitCurrency = creditLimitCurrency;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId,BigDecimal creditLimitAmount,
                         String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimitAmount = creditLimitAmount;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey,
                         BigDecimal interestRate) {
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

    public CreditCardDTO(Money balance, Long primaryOwnerId, BigDecimal creditLimitAmount,
                         Currency creditLimitCurrency, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimitAmount = creditLimitAmount;
        this.creditLimitCurrency = creditLimitCurrency;
    }

    public CreditCardDTO(Money balance, Long primaryOwnerId, BigDecimal creditLimitAmount, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.creditLimitAmount = creditLimitAmount;
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

    public BigDecimal getCreditLimitAmount() {
        return creditLimitAmount;
    }

    public void setCreditLimitAmount(BigDecimal creditLimitAmount) {
        this.creditLimitAmount = creditLimitAmount;
    }

    public Currency getCreditLimitCurrency() {
        return creditLimitCurrency;
    }

    public void setCreditLimitCurrency(Currency creditLimitCurrency) {
        this.creditLimitCurrency = creditLimitCurrency;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
