package com.ironhack.midterm_project.repository;

import com.ironhack.midterm_project.model.Transaction;
import com.ironhack.midterm_project.model.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query("SELECT t FROM Transaction t JOIN ThirdParty tp ON t.thirdParty = tp.id " +
            "WHERE tp.id = :thirdPartyId")
    List<Transaction> findTransactionsByThirdPartyId(@Param("thirdPartyId") Long thirdPartyId);

    @Query("SELECT t FROM Transaction t JOIN Account a ON t.sendingAccount = a.id OR t.receivingAccount = a.id " +
            "JOIN AccountHolder ah ON a.primaryOwner = ah.id OR a.secondaryOwner = ah.id " +
            "WHERE ah.id = :userId")
    List<Transaction> findTransactionsByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t JOIN Account a ON t.sendingAccount = a.id OR t.receivingAccount = a.id " +
            "WHERE a.id = :accountId")
    List<Transaction> findTransactionsByAccountId(@Param("accountId") Long accountId);
}
