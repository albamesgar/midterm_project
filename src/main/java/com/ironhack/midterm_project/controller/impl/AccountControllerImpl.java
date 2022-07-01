package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.CheckingDTO;
import com.ironhack.midterm_project.controller.dto.CreditCardDTO;
import com.ironhack.midterm_project.controller.dto.SavingsDTO;
import com.ironhack.midterm_project.controller.interfaces.AccountController;
import com.ironhack.midterm_project.model.accounts.*;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.repository.*;
import com.ironhack.midterm_project.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountControllerImpl implements AccountController {
    // See my account (account holder) -> GET
    // See all accounts (admin) -> GET
    // Create account (admin) -> POST
    // Set account balance (admin) -> PATCH

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

    // Show all accounts
    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> findAllAccounts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Account> accountList = accountRepository.findAll();
        for (Account account : accountList){
            account.getBalance();
            accountRepository.save(account);
        }
        return accountRepository.findAll();
    }

    // Show all checking accounts
    @GetMapping("/accounts/checkings")
    @ResponseStatus(HttpStatus.OK)
    public List<Checking> findAllChecking(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Checking> checkingList = checkingRepository.findAll();
        for (Checking checking : checkingList){
            checking.getBalance();
            checkingRepository.save(checking);
        }
        return checkingRepository.findAll();
    }

    // Show all student checking accounts
    @GetMapping("/accounts/student-checkings")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentChecking> findAllStudentChecking(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<StudentChecking> studentCheckingList = studentCheckingRepository.findAll();
        for (StudentChecking studentChecking : studentCheckingList){
            studentChecking.getBalance();
            studentCheckingRepository.save(studentChecking);
        }
        return studentCheckingRepository.findAll();
    }

    // Show all credit card accounts
    @GetMapping("/accounts/credit-cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> findAllCreditCard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        for (CreditCard creditCard : creditCardList){
            creditCard.getBalance();
            creditCardRepository.save(creditCard);
        }
        return creditCardRepository.findAll();
    }

    // Show all savings accounts
    @GetMapping("/accounts/savings")
    @ResponseStatus(HttpStatus.OK)
    public List<Savings> findAllSavings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Savings> savingsList = savingsRepository.findAll();
        for (Savings savings : savingsList){
            savings.getBalance();
            savingsRepository.save(savings);
        }
        return savingsRepository.findAll();
    }

    // Show all my accounts
    @GetMapping("/my-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> findAllMyAccounts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Account> accountList = accountRepository.findMyAccounts(userDetails.getUser().getId());
        for (Account account : accountList){
            account.getBalance();
            accountRepository.save(account);
        }
        return accountRepository.findMyAccounts(userDetails.getUser().getId());
    }

    // Show all my checking accounts
    @GetMapping("/my-accounts/checkings")
    @ResponseStatus(HttpStatus.OK)
    public List<Checking> findAllMyChecking(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Checking> checkingList = checkingRepository.findMyCheckingAccounts(userDetails.getUser().getId());
        for (Checking checking : checkingList){
            checking.getBalance();
            checkingRepository.save(checking);
        }
        return checkingRepository.findMyCheckingAccounts(userDetails.getUser().getId());
    }

    // Show all student checking accounts
    @GetMapping("/my-accounts/student-checkings")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentChecking> findAllMyStudentChecking(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<StudentChecking> studentCheckingList = studentCheckingRepository.findMyStudentCheckingAccounts(userDetails.getUser().getId());
        for (StudentChecking studentChecking : studentCheckingList){
            studentChecking.getBalance();
            studentCheckingRepository.save(studentChecking);
        }
        return studentCheckingRepository.findMyStudentCheckingAccounts(userDetails.getUser().getId());
    }

    // Show all my credit card accounts
    @GetMapping("/my-accounts/credit-cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> findAllMyCreditCard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CreditCard> creditCardList = creditCardRepository.findMyCreditCardAccounts(userDetails.getUser().getId());
        for (CreditCard creditCard : creditCardList){
            creditCard.getBalance();
            creditCardRepository.save(creditCard);
        }
        return creditCardRepository.findMyCreditCardAccounts(userDetails.getUser().getId());
    }

    // Show all my savings accounts
    @GetMapping("/my-accounts/savings")
    @ResponseStatus(HttpStatus.OK)
    public List<Savings> findAllMySavings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Savings> savingsList = savingsRepository.findMySavingsAccounts(userDetails.getUser().getId());
        for (Savings savings : savingsList){
            savings.getBalance();
            savingsRepository.save(savings);
        }
        return savingsRepository.findMySavingsAccounts(userDetails.getUser().getId());
    }

    // Show account by id (admin)
    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account findAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long id) { //Va en service
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account.getBalance();
        accountRepository.save(account);
        return account;
    }

    // Show my account by id (account holder)
    @GetMapping("/my-accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account findMyAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long id) { //Va en service
        Account account = accountRepository.findMyAccountById(userDetails.getUser().getId(),id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account.getBalance();
        accountRepository.save(account);
        return account;
    }

    // Create checking account if primary owner is older than 24 and student checking if he/she is younger
    @PostMapping("/new/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createChecking(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid CheckingDTO checkingDTO){
        Money balance = checkingDTO.getBalance();
        String secretKey = checkingDTO.getSecretKey();
        Long primaryOwnerId = checkingDTO.getPrimaryOwnerId();
        Optional<Long> optionalSecondaryOwnerId = checkingDTO.getSecondaryOwnerId();

        AccountHolder primaryOwner = accountHolderRepository.findById(primaryOwnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Holder not found"));
        int primaryOwnerAge = Period.between(primaryOwner.getDateOfBirth().toLocalDate(),LocalDate.now()).getYears();

        if(primaryOwnerAge>=24) {
            Checking checking = new Checking(balance, primaryOwner, secretKey, LocalDate.now());

            if (optionalSecondaryOwnerId.isPresent()) {
                AccountHolder secondaryOwner = accountHolderRepository.findById(optionalSecondaryOwnerId.get())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Holder not found"));
                checking = new Checking(balance, primaryOwner, secondaryOwner, secretKey, LocalDate.now());
            }
            return checkingRepository.save(checking);
        }

        StudentChecking studentChecking = new StudentChecking(balance, primaryOwner, secretKey, LocalDate.now());

        if (optionalSecondaryOwnerId.isPresent()) {
            AccountHolder secondaryOwner = accountHolderRepository.findById(optionalSecondaryOwnerId.get())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Holder not found"));
            studentChecking = new StudentChecking(balance, primaryOwner, secondaryOwner, secretKey, LocalDate.now());
        }
        return studentCheckingRepository.save(studentChecking);
    }

    // Create credit card account
    @PostMapping("/new/credit-card")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard createCreditCard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid CreditCardDTO creditCardDTO){
        Money balance = creditCardDTO.getBalance();
        String secretKey = creditCardDTO.getSecretKey();
        Money creditLimit = creditCardDTO.getCreditLimit();
        BigDecimal interestRate = creditCardDTO.getInterestRate();
        Long primaryOwnerId = creditCardDTO.getPrimaryOwnerId();
        Optional<Long> optionalSecondaryOwnerId = creditCardDTO.getSecondaryOwnerId();

        AccountHolder primaryOwner = accountHolderRepository.findById(primaryOwnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Holder not found"));

        CreditCard creditCard = new CreditCard(balance, primaryOwner, secretKey, creditLimit,
                    interestRate, LocalDate.now());

        if (optionalSecondaryOwnerId.isPresent()){
            AccountHolder secondaryOwner = accountHolderRepository.findById(optionalSecondaryOwnerId.get())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Holder not found"));
            creditCard = new CreditCard(balance, primaryOwner, secondaryOwner,secretKey,creditLimit,
                        interestRate,LocalDate.now());
        }
        return creditCardRepository.save(creditCard);
    }

    // Create savings account
    @PostMapping("/new/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Savings createSavingsAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid SavingsDTO savingsDTO){
        Money balance = savingsDTO.getBalance();
        String secretKey = savingsDTO.getSecretKey();
        Money minimumBalance = savingsDTO.getMinimumBalance();
        BigDecimal interestRate = savingsDTO.getInterestRate();
        Long primaryOwnerId = savingsDTO.getPrimaryOwnerId();
        Optional<Long> optionalSecondaryOwnerId = savingsDTO.getSecondaryOwnerId();

        AccountHolder primaryOwner = accountHolderRepository.findById(primaryOwnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Holder not found"));

        Savings savings = new Savings(balance, primaryOwner, secretKey, minimumBalance,
                    interestRate, LocalDate.now());

        if (optionalSecondaryOwnerId.isPresent()){
            AccountHolder secondaryOwner = accountHolderRepository.findById(optionalSecondaryOwnerId.get())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Holder not found"));
             savings = new Savings(balance, primaryOwner, secondaryOwner,secretKey,minimumBalance,
                        interestRate,LocalDate.now());
        }
        return savingsRepository.save(savings);
    }

    // Delete account by id (admin)
    @DeleteMapping("/delete/account/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long id) { //Va en service
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));;
        accountRepository.delete(account);
    }
}
