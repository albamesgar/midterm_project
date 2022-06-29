package com.ironhack.midterm_project.model.users;

import com.ironhack.midterm_project.model.accounts.Account;
import org.apache.tomcat.jni.Address;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Date;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AccountHolder extends User {
    private Date dateOfBirth;
    @Embedded
    private Address primaryAddress;
    private String mailingAddress;

    @OneToMany(mappedBy = "primaryOwner")
    private Set<Account> primaryAccountSet;

    @OneToMany(mappedBy = "secondaryOwner")
    private Set<Account> secondaryAccountSet;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public Set<Account> getPrimaryAccountSet() {
        return primaryAccountSet;
    }

    public void setPrimaryAccountSet(Set<Account> primaryAccountSet) {
        this.primaryAccountSet = primaryAccountSet;
    }

    public Set<Account> getSecondaryAccountSet() {
        return secondaryAccountSet;
    }

    public void setSecondaryAccountSet(Set<Account> secondaryAccountSet) {
        this.secondaryAccountSet = secondaryAccountSet;
    }

    // access to their own account balance
    // transfer money from any of their accounts to any other account
        // only if there are sufficient founds
        // provide Primary or Secondary owner name and id of the receiver account
}
