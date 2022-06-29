package com.ironhack.midterm_project.model.accounts;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.enums.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Checking extends Account{
    private Date creationDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private final Money minimumBalance = new Money(new BigDecimal(250)); //250

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency"))
    })
    private final Money monthlyMaintenanceFee = new Money(new BigDecimal(12)); //12

    private Date lastTimeMaintenanceFeeApplied;

    // CONSTRUCTORS
    public Checking() {
    }

    // GETTERS AND SETTERS
    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Deduct penalty fee if balance below minimum
    @Override
    public void setBalance(Money balance) {
        super.setBalance(balance);
        if (super.getBalance().getAmount().compareTo(minimumBalance.getAmount()) < 0){
            balance.decreaseAmount(super.getPenaltyFee());
        }
    }

    //if primary owner less than 24, create a studentchecking account
}
