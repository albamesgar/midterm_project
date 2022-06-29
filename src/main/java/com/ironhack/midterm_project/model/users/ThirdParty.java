package com.ironhack.midterm_project.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ThirdParty{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private String hashedKey; //Clave que se usa como mecanismo de autentificación
    private String name;

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

    // can receive and send money to other accounts
    // should be added to the database by an admin
    // to send and receive money -> provide hashed key in the header of HTTP request
        // provide amount, account id and account secret key
}
