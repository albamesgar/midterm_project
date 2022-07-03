package com.ironhack.midterm_project.service.impl;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.classes.TimeDifference;
import com.ironhack.midterm_project.model.accounts.*;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.repository.*;
import com.ironhack.midterm_project.service.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private SavingsRepository savingsRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    public void applyMonthlyMaintenanceFee(Checking checking){
        LocalDate lastTimeMaintenanceFeeApplied = checking.getLastTimeMaintenanceFeeApplied();
        int months = TimeDifference.monthDifference(lastTimeMaintenanceFeeApplied);
        if (months>0){
            Money balance = checking.getBalance();
            Money fee = checking.getMonthlyMaintenanceFee();
            BigDecimal feeToApply = fee.getAmount().multiply(BigDecimal.valueOf(months));
            balance.decreaseAmount(feeToApply);
            checking.setBalance(balance);
            checkingRepository.save(checking);
        }
    }

    public void applyMonthlyInterest(CreditCard creditCard){
        int months = TimeDifference.monthDifference(creditCard.getLastTimeInterestApplied());
        if (months>0){
            Money balance = creditCard.getBalance();
            BigDecimal interest = balance.getAmount().multiply(creditCard.getInterestRate()).
                    divide(new BigDecimal("12"));
            BigDecimal interestToApply = interest.multiply(BigDecimal.valueOf(months));
            balance.increaseAmount(interestToApply);
            creditCard.setBalance(balance);
            creditCardRepository.save(creditCard);
        }
    }

    public void applyAnnualInterest(Savings savings){
        int years = TimeDifference.yearDifference(savings.getLastTimeInterestApplied());
        if (years>0){
            Money balance = savings.getBalance();
            BigDecimal interest = balance.getAmount().multiply(savings.getInterestRate());
            BigDecimal interestToApply = interest.multiply(BigDecimal.valueOf(years));
            balance.increaseAmount(interestToApply);
            savings.setBalance(balance);
            savingsRepository.save(savings);
        }
    }

    public Account accountAutomaticActualization(Account account){
        switch (account.getAccountType()){
            case CHECKING:
                Checking checking = (Checking) account;
                applyMonthlyMaintenanceFee(checking);
                break;
            case CREDIT_CARD:
                CreditCard creditCard = (CreditCard) account;
                applyMonthlyInterest(creditCard);
                break;
            case SAVINGS:
                Savings savings = (Savings) account;
                applyAnnualInterest(savings);
                break;
        }
        return account;
    }

    public AccountHolder findAccountHolder(Long id){
        AccountHolder accountHolder = accountHolderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account Holder not found"));
        return  accountHolder;
    }

    public List<Account> findAllAccounts(){
        List<Account> accountList = accountRepository.findAll();
        for (Account account : accountList){
            account = accountAutomaticActualization(account);
        }
        return accountRepository.findAll();
    }

    public List<Checking> findAllChecking() {
        List<Checking> checkingList = checkingRepository.findAll();
        for (Checking checking : checkingList){
            applyMonthlyMaintenanceFee(checking);
        }
        return checkingRepository.findAll();
    }

    public List<CreditCard> findAllCreditCard() {
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        for (CreditCard creditCard : creditCardList){
            applyMonthlyInterest(creditCard);
        }
        return creditCardRepository.findAll();
    }

    public List<Savings> findAllSavings() {
        List<Savings> savingsList = savingsRepository.findAll();
        for (Savings savings : savingsList){
            applyAnnualInterest(savings);
        }
        return savingsRepository.findAll();
    }

    public List<Account> findAllMyAccounts(Long userId){
        List<Account> accountList = accountRepository.findMyAccounts(userId);
        for (Account account : accountList){
            account = accountAutomaticActualization(account);
        }
        return accountRepository.findMyAccounts(userId);
    }

    public List<Checking> findAllMyChecking(Long userId) {
        List<Checking> checkingList = checkingRepository.findMyCheckingAccounts(userId);
        for (Checking checking : checkingList){
            applyMonthlyMaintenanceFee(checking);
        }
        return checkingRepository.findMyCheckingAccounts(userId);
    }

    public List<CreditCard> findAllMyCreditCard(Long userId) {
        List<CreditCard> creditCardList = creditCardRepository.findMyCreditCardAccounts(userId);
        for (CreditCard creditCard : creditCardList){
            applyMonthlyInterest(creditCard);
        }
        return creditCardRepository.findMyCreditCardAccounts(userId);
    }

    public List<Savings> findAllMySavings(Long userId) {
        List<Savings> savingsList = savingsRepository.findMySavingsAccounts(userId);
        for (Savings savings : savingsList){
            applyAnnualInterest(savings);
        }
        return savingsRepository.findMySavingsAccounts(userId);
    }

    public Account findAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account = accountAutomaticActualization(account);
        return account;
    }

    public Account findMyAccount(Long userId, Long id) {
        Account account = accountRepository.findMyAccountById(userId,id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account = accountAutomaticActualization(account);
        return account;
    }

    public Account checkingLogic(Money balance,String secretKey,Long primaryOwnerId,
                                 Long optionalSecondaryOwnerId){
        secretKey = UUID.nameUUIDFromBytes(secretKey.getBytes()).toString();
        AccountHolder primaryOwner = findAccountHolder(primaryOwnerId);
        int primaryOwnerAge = Period.between(primaryOwner.getDateOfBirth().toLocalDate(),LocalDate.now()).getYears();

        if(primaryOwnerAge>=24) {
            Checking checking = new Checking(balance, primaryOwner, secretKey, LocalDate.now());

            if (optionalSecondaryOwnerId!=0L) {
                AccountHolder secondaryOwner = findAccountHolder(optionalSecondaryOwnerId);
                checking = new Checking(balance, primaryOwner, secondaryOwner, secretKey, LocalDate.now());
            }
            return checking;
        }

        StudentChecking studentChecking = new StudentChecking(balance, primaryOwner, secretKey, LocalDate.now());

        if (optionalSecondaryOwnerId!=0L) {
            AccountHolder secondaryOwner = findAccountHolder(optionalSecondaryOwnerId);
            studentChecking = new StudentChecking(balance, primaryOwner, secondaryOwner, secretKey, LocalDate.now());
        }
        return studentChecking;
    }

    public Account createChecking(Money balance,String secretKey,Long primaryOwnerId,
                                  Long optionalSecondaryOwnerId) {
        Account account = checkingLogic(balance,secretKey,primaryOwnerId,optionalSecondaryOwnerId);
        return accountRepository.save(account);
    }

    public void modifyCheckingAccount(Money balance,String secretKey,Long primaryOwnerId,
                               Long optionalSecondaryOwnerId,Long id){
        Account originalAccount = findAccount(id);
        Account account = checkingLogic(balance,secretKey,primaryOwnerId,optionalSecondaryOwnerId);
        account.setId(originalAccount.getId());
        accountRepository.save(account);
    }

    public CreditCard creditCardLogic(Money balance,String secretKey,Money creditLimit,BigDecimal interestRate,
                                      Long primaryOwnerId, Long optionalSecondaryOwnerId){
        secretKey = UUID.nameUUIDFromBytes(secretKey.getBytes()).toString();
        AccountHolder primaryOwner = findAccountHolder(primaryOwnerId);

        CreditCard creditCard = new CreditCard(balance, primaryOwner, secretKey, creditLimit,
                interestRate, LocalDate.now());

        if (optionalSecondaryOwnerId!=0L){
            AccountHolder secondaryOwner = findAccountHolder(optionalSecondaryOwnerId);
            creditCard = new CreditCard(balance, primaryOwner, secondaryOwner,secretKey,creditLimit,
                    interestRate,LocalDate.now());
        }
        return creditCard;
    }

    public CreditCard createCreditCard(Money balance,String secretKey,Money creditLimit,BigDecimal interestRate,
                                       Long primaryOwnerId, Long optionalSecondaryOwnerId){
        CreditCard creditCard = creditCardLogic(balance,secretKey,creditLimit,interestRate,primaryOwnerId,
                optionalSecondaryOwnerId);
        return creditCardRepository.save(creditCard);
    }

    public void modifyCreditCardAccount(Money balance,String secretKey,Money creditLimit,BigDecimal interestRate,
                                        Long primaryOwnerId, Long optionalSecondaryOwnerId, Long id){
        Account originalAccount = findAccount(id);
        CreditCard creditCard = creditCardLogic(balance,secretKey,creditLimit,interestRate,primaryOwnerId,
                optionalSecondaryOwnerId);
        creditCard.setId(originalAccount.getId());
        creditCardRepository.save(creditCard);
    }

    public Savings savingsLogic(Money balance,String secretKey,Money minimumBalance,BigDecimal interestRate,
                                Long primaryOwnerId,Long optionalSecondaryOwnerId){
        secretKey = UUID.nameUUIDFromBytes(secretKey.getBytes()).toString();
        AccountHolder primaryOwner = findAccountHolder(primaryOwnerId);

        Savings savings = new Savings(balance, primaryOwner, secretKey, minimumBalance,
                interestRate, LocalDate.now());

        if (optionalSecondaryOwnerId!=0L){
            AccountHolder secondaryOwner = findAccountHolder(optionalSecondaryOwnerId);
            savings = new Savings(balance, primaryOwner, secondaryOwner,secretKey,minimumBalance,
                    interestRate,LocalDate.now());
        }
        return savings;
    }

    public Savings createSavingsAccount(Money balance,String secretKey,Money minimumBalance,BigDecimal interestRate,
                                        Long primaryOwnerId,Long optionalSecondaryOwnerId){
        Savings savings = savingsLogic(balance,secretKey,minimumBalance,interestRate,primaryOwnerId,
                optionalSecondaryOwnerId);
        return savingsRepository.save(savings);
    }

    public void modifySavingsAccount(Money balance,String secretKey,Money minimumBalance,BigDecimal interestRate,
                                     Long primaryOwnerId,Long optionalSecondaryOwnerId,Long id){
        Account originalAccount = findAccount(id);
        Savings savings = savingsLogic(balance,secretKey,minimumBalance,interestRate,primaryOwnerId,
                optionalSecondaryOwnerId);
        savings.setId(originalAccount.getId());
        savingsRepository.save(savings);
    }

    public void modifyBalance(Long id,Money newBalance){
        Account account = findAccount(id);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
