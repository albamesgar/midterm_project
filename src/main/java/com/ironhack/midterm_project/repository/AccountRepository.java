package com.ironhack.midterm_project.repository;

import com.ironhack.midterm_project.model.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    @Query("SELECT a FROM Account a JOIN AccountHolder ah ON a.primaryOwner = ah.id " +
            "OR a.secondaryOwner = ah.id WHERE ah.id = :id")
    List<Account> findMyAccounts(@Param("id") Long id);

    @Query("SELECT a FROM Account a JOIN AccountHolder ah ON a.primaryOwner = ah.id OR a.secondaryOwner = ah.id " +
            "WHERE ah.id = :userId AND a.id = :accountId")
    Optional<Account> findMyAccountById(@Param("userId") Long userId, @Param("accountId") Long accountId);
}
