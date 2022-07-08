package com.ironhack.midterm_project.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm_project.model.Transaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class ThirdParty{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Hashed key can not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String hashedKey;
    @NotNull(message = "Name can not be null")
    private String name;

    @OneToMany(mappedBy = "thirdParty")
    @JsonIgnore
    private Set<Transaction> transactionsDone = new HashSet<>();

    //CONSTRUCTORS
    public ThirdParty() {
    }

    public ThirdParty(String hashedKey, String name) {
        this.hashedKey = hashedKey;
        this.name = name;
    }

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Transaction> getTransactionsDone() {
        return transactionsDone;
    }

    public void setTransactionsDone(Set<Transaction> transactionsDone) {
        this.transactionsDone = transactionsDone;
    }

    //Is equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThirdParty that = (ThirdParty) o;
        return hashedKey.equals(that.hashedKey) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedKey, name);
    }
}
