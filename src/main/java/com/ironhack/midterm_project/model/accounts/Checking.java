package com.ironhack.midterm_project.model.accounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.classes.TimeDifference;
import com.ironhack.midterm_project.enums.AccountType;
import com.ironhack.midterm_project.enums.Status;
import com.ironhack.midterm_project.model.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Checking extends Account{
//    @Null(message = "You can not change the minimum balance")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private final Money minimumBalance; //250

//    @Null(message = "You can not change the monthly maintenance fee")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency"))
    })
    private final Money monthlyMaintenanceFee; //12
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastTimeMaintenanceFeeApplied;

    // CONSTRUCTORS
    public Checking() {
        this.lastTimeMaintenanceFeeApplied = this.creationDate;
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
        this.minimumBalance = new Money(new BigDecimal(250));
    }

    public Checking(Money balance, AccountHolder primaryOwner, String secretKey, LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.lastTimeMaintenanceFeeApplied = this.creationDate;
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
        this.minimumBalance = new Money(new BigDecimal(250));
    }

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey,
                    LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.lastTimeMaintenanceFeeApplied = this.creationDate;
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
        this.minimumBalance = new Money(new BigDecimal(250));
    }

    // GETTERS AND SETTERS
    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
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
//        if (TimeDifference.monthDifference(lastTimeMaintenanceFeeApplied)){
//            Money balance = super.getBalance();
//            Money fee = getMonthlyMaintenanceFee();
//            balance.decreaseAmount(fee);
//            super.setBalance(balance);
//        }
        return super.getBalance();
    }

    //if primary owner less than 24, create a studentchecking account
}
