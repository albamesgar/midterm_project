package com.ironhack.midterm_project.model.accounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.classes.TimeDifference;
import com.ironhack.midterm_project.enums.AccountType;
import com.ironhack.midterm_project.model.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends Account{
    @Embedded
//    @DecimalMax(value = "100000", message = "The money credit can not be higher than 100000")
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency"))
    })
    private Money creditLimit; //default 100, max 100000 when instantiated

    @DecimalMin(value = "0.1", message = "The interest rate can not be lower than 0.1")
    private BigDecimal interestRate; //default 0.2, min 0.1 when instantiated
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastTimeInterestApplied;

    // the interest*balance is added monthly to the balance (interest = interestRate/12)

    // CONSTRUCTORS
    public CreditCard() {
        this.creditLimit = new Money(new BigDecimal(100));
        this.interestRate = new BigDecimal("0.2");
        this.lastTimeInterestApplied = this.creationDate;
        this.accountType = AccountType.CREDIT_CARD;
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, String secretKey, Money creditLimit,
                      BigDecimal interestRate, LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.lastTimeInterestApplied = this.creationDate;
        this.accountType = AccountType.CREDIT_CARD;
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey,
                      Money creditLimit, BigDecimal interestRate, LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.lastTimeInterestApplied = this.creationDate;
        this.accountType = AccountType.CREDIT_CARD;
    }

    // GETTERS AND SETTERS
    public void setLastTimeInterestApplied(LocalDate lastTimeInterestApplied) {
        this.lastTimeInterestApplied = lastTimeInterestApplied;
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

    public LocalDate getLastTimeInterestApplied() {
        return lastTimeInterestApplied;
    }

    @Override
    public Money getBalance() {
        if (TimeDifference.monthDifference(lastTimeInterestApplied)){
            Money balance = super.getBalance();
            BigDecimal interest = balance.getAmount().multiply(interestRate).divide(new BigDecimal("12"));
            balance.increaseAmount(interest);
            super.setBalance(balance);
        }
        return super.getBalance();
    }

}
