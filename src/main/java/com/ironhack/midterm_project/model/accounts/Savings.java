package com.ironhack.midterm_project.model.accounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.classes.TimeDifference;
import com.ironhack.midterm_project.enums.AccountType;
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
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance; //default 1000, min 100 when instantiated

    @DecimalMax(value = "0.5", message = "The interest rate can not be lower than 0.5")
    private BigDecimal interestRate; //Default 0.0025, max 0.5
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastTimeInterestApplied;

    // CONSTRUCTORS
    public Savings() {
        this.minimumBalance = new Money(new BigDecimal(1000));
        this.interestRate = new BigDecimal("0.0025");
        this.lastTimeInterestApplied = this.creationDate;
        this.accountType = AccountType.SAVINGS;
    }

    public Savings(Money balance, AccountHolder primaryOwner, String secretKey, Money minimumBalance,
                   BigDecimal interestRate, LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.lastTimeInterestApplied = this.creationDate;
        this.accountType = AccountType.SAVINGS;
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   String secretKey, Money minimumBalance, BigDecimal interestRate, LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.lastTimeInterestApplied = this.creationDate;
        this.accountType = AccountType.SAVINGS;
    }

    // GETTERS AND SETTERS
    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LocalDate getLastTimeInterestApplied() {
        return lastTimeInterestApplied;
    }

    public void setLastTimeInterestApplied(LocalDate lastTimeInterestApplied) {
        this.lastTimeInterestApplied = lastTimeInterestApplied;
    }

    // Deduct penalty fee if balance below minimum
    @Override
    public void setBalance(Money balance) {
        super.setBalance(balance);
        Money actualBalance = new Money(getBalance().getAmount(),getBalance().getCurrency());
        actualBalance.decreaseAmount(minimumBalance);
        if (actualBalance.getAmount().compareTo(BigDecimal.valueOf(0)) < 0){
            balance.decreaseAmount(super.getPenaltyFee());
        }
    }
}
