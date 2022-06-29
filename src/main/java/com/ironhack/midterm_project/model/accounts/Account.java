package com.ironhack.midterm_project.model.accounts;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.enums.AccountType;
import com.ironhack.midterm_project.model.Transaction;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({ //Para insertar un nuevo campo del mismo tipo
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency"))
    })
    private Money balance;

    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;

    @Embedded
    @AttributeOverrides({ //Para insertar un nuevo campo del mismo tipo
            @AttributeOverride(name = "amount", column = @Column(name = "penalty_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "penalty_fee_currency"))
    })
    private final Money penaltyFee = new Money(new BigDecimal(40)); //If balance below minimBalance penaltyFee deducted
    private int secretKey;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @OneToMany(mappedBy = "sendingAccount")
    private Set<Transaction> transactionsDone;
    @OneToMany(mappedBy = "receivingAccount")
    private Set<Transaction> transactionsReceived;

    // CONSTRUCTORS
    public Account() {
    }

    // GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Money getBalance() {
        return balance;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public Money getPenaltyFee() {
        return penaltyFee;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public int getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(int secretKey) {
        this.secretKey = secretKey;
    }
}
