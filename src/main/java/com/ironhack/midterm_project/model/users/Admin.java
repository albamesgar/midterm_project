package com.ironhack.midterm_project.model.users;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User{
    //can access the balance of any account and modify it

//    public Admin() {
//        Role role =new Role();
//        role.setName("ADMIN");
//        this.setRole(role);
//    }
}
