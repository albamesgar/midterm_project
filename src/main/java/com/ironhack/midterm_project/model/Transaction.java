package com.ironhack.midterm_project.model;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.enums.TransactionType;
import com.ironhack.midterm_project.model.accounts.Account;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.ThirdParty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private Money amount;

//    @OneToOne
//    private AccountHolder sendingUser;
    @ManyToOne
    private Account sendingAccount;

    @ManyToOne
    private Account receivingAccount;

    @OneToOne
    private ThirdParty sendingThirdParty;

}
