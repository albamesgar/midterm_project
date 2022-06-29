package com.ironhack.midterm_project.model.accounts;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.model.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends Account{
    @Embedded
    @DecimalMax("100000")
    @AttributeOverrides({ //Para insertar un nuevo campo del mismo tipo
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency"))
    })
    private Money creditLimit = new Money(new BigDecimal(100)); //default 100, max 100000 when instantiated

    @DecimalMin("0.1")
    private BigDecimal interestRate = new BigDecimal(0.2); //default 0.2, min 0.1 when instantiated

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
}
