package com.ironhack.midterm_project.model.accounts;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.enums.AccountType;
import com.ironhack.midterm_project.enums.Status;
import com.ironhack.midterm_project.model.users.AccountHolder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class StudentChecking extends Account{

    // CONSTRUCTORS
    public StudentChecking() {
        this.accountType = AccountType.STUDENT_CHECKING;
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, String secretKey, LocalDate creationDate) {
        super(balance, primaryOwner, secretKey, creationDate);
        this.accountType = AccountType.STUDENT_CHECKING;
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey,
                           LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner, secretKey, creationDate);
        this.accountType = AccountType.STUDENT_CHECKING;
    }
}
