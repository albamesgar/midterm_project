package com.ironhack.midterm_project.model.accounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.classes.TimeDifference;
import com.ironhack.midterm_project.enums.AccountType;
import com.ironhack.midterm_project.model.users.AccountHolder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Checking extends Account{
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private final Money minimumBalance; //250

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
        this.accountType = AccountType.CHECKING;
    }

    public Checking(Money balance, AccountHolder primaryOwner, String secretKey, LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.lastTimeMaintenanceFeeApplied = this.creationDate;
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
        this.minimumBalance = new Money(new BigDecimal(250));
        this.accountType = AccountType.CHECKING;
    }

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey,
                    LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.lastTimeMaintenanceFeeApplied = this.creationDate;
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
        this.minimumBalance = new Money(new BigDecimal(250));
        this.accountType = AccountType.CHECKING;
    }

    // GETTERS AND SETTERS
    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public LocalDate getLastTimeMaintenanceFeeApplied() {
        return lastTimeMaintenanceFeeApplied;
    }

    public void setLastTimeMaintenanceFeeApplied(LocalDate lastTimeMaintenanceFeeApplied) {
        this.lastTimeMaintenanceFeeApplied = lastTimeMaintenanceFeeApplied;
    }

    // Deduct penalty fee if balance below minimum
    @Override
    public void setBalance(Money balance) {
        super.setBalance(balance);
        Money actualBalance = new Money(getBalance().getAmount(),getBalance().getCurrency());
        actualBalance.decreaseAmount(minimumBalance);
        if (actualBalance.getAmount().compareTo(BigDecimal.ZERO) < 0){
            balance.decreaseAmount(super.getPenaltyFee());
        }
    }
}
