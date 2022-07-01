package com.ironhack.midterm_project.repository;

import com.ironhack.midterm_project.model.accounts.Checking;
import com.ironhack.midterm_project.model.accounts.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard,Long> {
    @Query("SELECT a FROM CreditCard a JOIN AccountHolder ah ON a.primaryOwner = ah.id " +
            "OR a.secondaryOwner = ah.id WHERE ah.id = :id")
    List<CreditCard> findMyCreditCardAccounts(@Param("id") Long id);

    @Query("SELECT a FROM CreditCard a JOIN AccountHolder ah ON a.primaryOwner = ah.id OR a.secondaryOwner = ah.id " +
            "WHERE ah.id = :userId AND a.id = :accountId")
    Optional<CreditCard> findMyCreditCardAccountById(@Param("userId") Long userId, @Param("accountId") Long accountId);
}
