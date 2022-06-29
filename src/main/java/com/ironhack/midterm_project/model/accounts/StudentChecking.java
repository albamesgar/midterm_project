package com.ironhack.midterm_project.model.accounts;

import com.ironhack.midterm_project.enums.Status;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class StudentChecking extends Account{
    private Date creationDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    // CONSTRUCTORS
    public StudentChecking() {
    }

    // GETTERS AND SETTERS
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
