package com.ironhack.midterm_project.model.accounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.classes.TimeDifference;
import com.ironhack.midterm_project.enums.Status;
import com.ironhack.midterm_project.model.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Savings extends Account{
    @Embedded
//    @DecimalMin(value = "100", message = "The minimum balance can not be lower than 100")
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance; //default 1000, min 100 when instantiated

//    @DecimalMax(value = "0.5", message = "The interest rate can not be lower than 0.5")
    private BigDecimal interestRate; //Default 0.0025, max 0.5
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastTimeInterestApplied;

    // after one year the balance*interestRate is added to the balance

    // CONSTRUCTORS
    public Savings() {
        this.minimumBalance = new Money(new BigDecimal(1000));
        this.interestRate = new BigDecimal(0.0025);
        this.lastTimeInterestApplied = this.creationDate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, String secretKey,
                   LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.minimumBalance = new Money(new BigDecimal(1000));
        this.interestRate = new BigDecimal(0.0025);
        this.lastTimeInterestApplied = this.creationDate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey,
                   LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.minimumBalance = new Money(new BigDecimal(1000));
        this.interestRate = new BigDecimal(0.0025);
        this.lastTimeInterestApplied = this.creationDate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, String secretKey, Money minimumBalance,
                   BigDecimal interestRate, LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.lastTimeInterestApplied = this.creationDate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, String secretKey, BigDecimal interestRate,
                   LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.minimumBalance = new Money(new BigDecimal(1000));
        this.interestRate = interestRate;
        this.lastTimeInterestApplied = this.creationDate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, String secretKey, Money minimumBalance,
                   LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.minimumBalance = minimumBalance;
        this.interestRate = new BigDecimal(0.0025);
        this.lastTimeInterestApplied = this.creationDate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   String secretKey, Money minimumBalance, BigDecimal interestRate, LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.lastTimeInterestApplied = this.creationDate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   String secretKey, BigDecimal interestRate, LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.minimumBalance = new Money(new BigDecimal(1000));
        this.interestRate = interestRate;
        this.lastTimeInterestApplied = this.creationDate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   String secretKey, Money minimumBalance, LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.minimumBalance = minimumBalance;
        this.interestRate = new BigDecimal(0.0025);
        this.lastTimeInterestApplied = this.creationDate;
    }

    // GETTERS AND SETTERS
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

    // Deduct penalty fee if balance below minimum
    @Override
    public void setBalance(Money balance) {
        super.setBalance(balance);
        if (super.getBalance().getAmount().compareTo(minimumBalance.getAmount()) < 0){
            balance.decreaseAmount(super.getPenaltyFee());
        }
    }

    @Override
    public Money getBalance() {
//        if (TimeDifference.yearDifference(lastTimeInterestApplied)){
//            Money balance = super.getBalance();
//            BigDecimal interest = balance.getAmount().multiply(interestRate);
//            balance.increaseAmount(interest);
//            super.setBalance(balance);
//        }
        return super.getBalance();
    }
}
