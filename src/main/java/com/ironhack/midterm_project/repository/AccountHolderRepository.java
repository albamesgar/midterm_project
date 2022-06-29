package com.ironhack.midterm_project.repository;

import com.ironhack.midterm_project.model.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder,Long> {
}
