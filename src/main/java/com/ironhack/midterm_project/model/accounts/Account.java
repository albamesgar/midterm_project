package com.ironhack.midterm_project.model.accounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.enums.AccountType;
import com.ironhack.midterm_project.enums.Status;
import com.ironhack.midterm_project.model.Transaction;
import com.ironhack.midterm_project.model.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Initial balance can not be null")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency"))
    })
    private Money balance;

    @NotNull(message = "Primary owner can not be null")
    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;

    @Null(message = "You can not change the penalty fee")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "penalty_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "penalty_fee_currency"))
    })
    private final Money penaltyFee;
    @NotNull(message = "Secret key can not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secretKey;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "sendingAccount")
    private Set<Transaction> transactionsDone;
    @OneToMany(mappedBy = "receivingAccount")
    private Set<Transaction> transactionsReceived;

    // CONSTRUCTORS
    public Account() {
        this.penaltyFee = new Money(new BigDecimal(40));
        this.balance = new Money(new BigDecimal(0));
        this.status = Status.ACTIVE;
    }

    public Account(Money balance, AccountHolder primaryOwner, String secretKey, LocalDate creationDate){
        this.balance = balance;
        this.penaltyFee = new Money(new BigDecimal(40));
        this.primaryOwner = primaryOwner;
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.status = Status.ACTIVE;
    }

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   String secretKey, LocalDate creationDate){
        this.balance = balance;
        this.penaltyFee = new Money(new BigDecimal(40));
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.status = Status.ACTIVE;
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

    public String  getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
