package com.ironhack.midterm_project.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String name;
    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<User> userSet;

    //CONSTRUCTORS
    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
