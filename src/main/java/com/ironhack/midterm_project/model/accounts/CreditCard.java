package com.ironhack.midterm_project.model.accounts;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.model.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends Account{
    @Embedded
    @DecimalMax(value = "100000", message = "The money credit can not be higher than 100000")
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency"))
    })
    private Money creditLimit = new Money(new BigDecimal(100)); //default 100, max 100000 when instantiated

    @DecimalMin(value = "0.1", message = "The interest rate can not be lower than 0.1")
    private BigDecimal interestRate = new BigDecimal(0.2); //default 0.2, min 0.1 when instantiated

    private Date lastTimeInterestApplied;

    // the interest*balance is added monthly to the balance (interest = interestRate/12)

    // CONSTRUCTORS
    public CreditCard() {
    }

    // GETTERS AND SETTERS
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

    public Date getLastTimeInterestApplied() {
        return lastTimeInterestApplied;
    }

    public void setLastTimeInterestApplied(Date lastTimeInterestApplied) {
        this.lastTimeInterestApplied = lastTimeInterestApplied;
    }
}
