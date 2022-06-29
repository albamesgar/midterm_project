package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.controller.interfaces.AccountController;
import com.ironhack.midterm_project.model.accounts.*;
import com.ironhack.midterm_project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    // Show all accounts
    @GetMapping("/accounts")
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    // Show all checking accounts
    @GetMapping("/accounts/checking")
    public List<Checking> findAllChecking() {
        return checkingRepository.findAll();
    }

    // Show all student checking accounts
    @GetMapping("/accounts/student-checking")
    public List<StudentChecking> findAllStudentChecking() {
        return studentCheckingRepository.findAll();
    }

    // Show all credit card accounts
    @GetMapping("/accounts/credit-card")
    public List<CreditCard> findAllCreditCard() {
        return creditCardRepository.findAll();
    }

    // Show all savings accounts
    @GetMapping("/accounts/savings")
    public List<Savings> findAllSavings() {
        return savingsRepository.findAll();
    }

    // Show account by id
    @GetMapping("/accounts/{id}")
    public Account findAccount(@PathVariable Long id) { //Va en service
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return account;
    }

    // Show checking account by id
    @GetMapping("/accounts/checking/{id}")
    public Checking findChecking(@PathVariable Long id) {
        Checking checking = checkingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return checking;
    }

    // Show student checking account by id
    @GetMapping("/accounts/student-checking/{id}")
    public StudentChecking findStudentChecking(@PathVariable Long id) {
        StudentChecking studentChecking = studentCheckingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return studentChecking;
    }

    // Show credit card account by id
    @GetMapping("/accounts/credit-card/{id}")
    public CreditCard findCreditCard(@PathVariable Long id) {
        CreditCard creditCard = creditCardRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return creditCard;
    }

    // Show savings account by id
    @GetMapping("/accounts/savings/{id}")
    public Savings findSavings(@PathVariable Long id) {
        Savings savings = savingsRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return savings;
    }
}
