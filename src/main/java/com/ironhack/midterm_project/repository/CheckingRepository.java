package com.ironhack.midterm_project.repository;

import com.ironhack.midterm_project.model.accounts.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingRepository extends JpaRepository<Checking,Long> {
}
