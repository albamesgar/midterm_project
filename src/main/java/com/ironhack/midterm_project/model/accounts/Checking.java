package com.ironhack.midterm_project.model.accounts;

import com.ironhack.midterm_project.classes.Money;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Checking extends StudentChecking{
    @Embedded
    @AttributeOverrides({ //Para insertar un nuevo campo del mismo tipo
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private final Money minimumBalance = new Money(new BigDecimal(250)); //250

    @Embedded
    @AttributeOverrides({ //Para insertar un nuevo campo del mismo tipo
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency"))
    })
    private final Money monthlyMaintenanceFee = new Money(new BigDecimal(12)); //12

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

    //if primary owner less than 24, create a studentchecking account
}
