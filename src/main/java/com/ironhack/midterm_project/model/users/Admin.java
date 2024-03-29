package com.ironhack.midterm_project.model.users;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User{
    //CONSTRUCTORS
    public Admin() {
    }

    public Admin(String username, String password, Role role) {
        super(username, password, role);
    }

    //Is equal
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
