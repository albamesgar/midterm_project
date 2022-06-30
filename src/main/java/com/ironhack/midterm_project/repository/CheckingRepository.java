package com.ironhack.midterm_project.repository;

import com.ironhack.midterm_project.model.accounts.Account;
import com.ironhack.midterm_project.model.accounts.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckingRepository extends JpaRepository<Checking,Long> {
    @Query("SELECT a FROM Checking a JOIN AccountHolder ah ON a.primaryOwner = ah.id WHERE ah.id = :id")
    List<Checking> findMyCheckingAccounts(@Param("id") Long id);

    @Query("SELECT a FROM Checking a JOIN AccountHolder ah ON a.primaryOwner = ah.id WHERE ah.id = :userId AND " +
            "a.id = :accountId")
    Optional<Checking> findMyCheckingAccountById(@Param("userId") Long userId, @Param("accountId") Long accountId);
}
