package com.ironhack.midterm_project.service.interfaces;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.model.accounts.Account;
import com.ironhack.midterm_project.model.accounts.Checking;
import com.ironhack.midterm_project.model.accounts.CreditCard;
import com.ironhack.midterm_project.model.accounts.Savings;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<Account> findAllAccounts();
    List<Checking> findAllChecking();
    List<CreditCard> findAllCreditCard();
    List<Savings> findAllSavings();
    List<Account> findAllMyAccounts(Long userId);
    List<Checking> findAllMyChecking(Long userId);
    List<CreditCard> findAllMyCreditCard(Long userId);
    List<Savings> findAllMySavings(Long userId);
    Account findAccount(Long id);
    Account findMyAccount(Long userId, Long id);
    Account createChecking(Money balance, String secretKey, Long primaryOwnerId,
                           Long optionalSecondaryOwnerId);
    void modifyCheckingAccount(Money balance,String secretKey,Long primaryOwnerId,
                          Long optionalSecondaryOwnerId,Long id);
    CreditCard createCreditCard(Money balance, String secretKey, Money creditLimit, BigDecimal interestRate,
                     Long primaryOwnerId, Long optionalSecondaryOwnerId);
    void modifyCreditCardAccount(Money balance,String secretKey,Money creditLimit,BigDecimal interestRate,
                                 Long primaryOwnerId, Long optionalSecondaryOwnerId, Long id);
    Savings createSavingsAccount(Money balance,String secretKey,Money minimumBalance,BigDecimal interestRate,
                                 Long primaryOwnerId,Long optionalSecondaryOwnerId);
    void modifySavingsAccount(Money balance,String secretKey,Money minimumBalance,BigDecimal interestRate,
                              Long primaryOwnerId,Long optionalSecondaryOwnerId,Long id);
    void modifyBalance(Long id,Money newBalance);
}
