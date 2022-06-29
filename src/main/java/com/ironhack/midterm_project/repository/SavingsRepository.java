package com.ironhack.midterm_project.repository;

import com.ironhack.midterm_project.model.accounts.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<Savings,Long> {
}
