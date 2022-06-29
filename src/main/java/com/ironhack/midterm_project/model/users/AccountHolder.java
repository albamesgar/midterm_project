package com.ironhack.midterm_project.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.midterm_project.model.accounts.Account;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AccountHolder extends User {
    private Date dateOfBirth;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "primary_address_street")),
            @AttributeOverride(name = "homeNumber", column = @Column(name = "primary_address_home_number")),
            @AttributeOverride(name = "city", column = @Column(name = "primary_address_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "primary_address_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "primary_address_country"))
    })
    private Address primaryAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "mailing_address_street")),
            @AttributeOverride(name = "homeNumber", column = @Column(name = "mailing_address_home_number")),
            @AttributeOverride(name = "city", column = @Column(name = "mailing_address_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "mailing_address_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "mailing_address_country"))
    })
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryOwner")
    @JsonIgnore
    private Set<Account> primaryAccountSet;

    @OneToMany(mappedBy = "secondaryOwner")
    @JsonIgnore
    private Set<Account> secondaryAccountSet;

    // GETTERS AND SETTERS
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

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
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
