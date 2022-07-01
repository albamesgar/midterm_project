package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.*;
import com.ironhack.midterm_project.controller.interfaces.AccountController;
import com.ironhack.midterm_project.model.accounts.*;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.repository.*;
import com.ironhack.midterm_project.security.CustomUserDetails;
import com.ironhack.midterm_project.service.interfaces.AccountService;
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
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;
    @Autowired
    private AccountService accountService;

    // Show all accounts
    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> findAllAccounts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return accountService.findAllAccounts();
    }

    // Show all checking accounts
    @GetMapping("/accounts/checkings")
    @ResponseStatus(HttpStatus.OK)
    public List<Checking> findAllChecking(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return accountService.findAllChecking();
    }

    // Show all student checking accounts
    @GetMapping("/accounts/student-checkings")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentChecking> findAllStudentChecking(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return studentCheckingRepository.findAll();
    }

    // Show all credit card accounts
    @GetMapping("/accounts/credit-cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> findAllCreditCard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return accountService.findAllCreditCard();
    }

    // Show all savings accounts
    @GetMapping("/accounts/savings")
    @ResponseStatus(HttpStatus.OK)
    public List<Savings> findAllSavings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return accountService.findAllSavings();
    }

    // Show all my accounts
    @GetMapping("/my-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> findAllMyAccounts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        return accountService.findAllMyAccounts(userId);
    }

    // Show all my checking accounts
    @GetMapping("/my-accounts/checkings")
    @ResponseStatus(HttpStatus.OK)
    public List<Checking> findAllMyChecking(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();

        return accountService.findAllMyChecking(userId);
    }

    // Show all student checking accounts
    @GetMapping("/my-accounts/student-checkings")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentChecking> findAllMyStudentChecking(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        return studentCheckingRepository.findMyStudentCheckingAccounts(userId);
    }

    // Show all my credit card accounts
    @GetMapping("/my-accounts/credit-cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> findAllMyCreditCard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();

        return accountService.findAllMyCreditCard(userId);
    }

    // Show all my savings accounts
    @GetMapping("/my-accounts/savings")
    @ResponseStatus(HttpStatus.OK)
    public List<Savings> findAllMySavings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();

        return accountService.findAllMySavings(userId);
    }

    // Show account by id (admin)
    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account findAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long id) {
        return accountService.findAccount(id);
    }

    // Show my account by id (account holder)
    @GetMapping("/my-accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account findMyAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long id) {
        Long userId = userDetails.getUser().getId();

        return accountService.findMyAccount(userId,id);
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

        return accountService.createChecking(balance,secretKey,primaryOwnerId,optionalSecondaryOwnerId);
    }

    // Create credit card account
    @PostMapping("/new/credit-card")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard createCreditCard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid CreditCardDTO creditCardDTO){
        Money balance = creditCardDTO.getBalance();
        String secretKey = creditCardDTO.getSecretKey();
        Money creditLimit = new Money(creditCardDTO.getCreditLimitAmount(), creditCardDTO.getCreditLimitCurrency());
        BigDecimal interestRate = creditCardDTO.getInterestRate();
        Long primaryOwnerId = creditCardDTO.getPrimaryOwnerId();
        Optional<Long> optionalSecondaryOwnerId = creditCardDTO.getSecondaryOwnerId();

        return accountService.createCreditCard(balance,secretKey,creditLimit,interestRate,primaryOwnerId,
                optionalSecondaryOwnerId);
    }

    // Create savings account
    @PostMapping("/new/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Savings createSavingsAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid SavingsDTO savingsDTO){
        Money balance = savingsDTO.getBalance();
        String secretKey = savingsDTO.getSecretKey();
        Money minimumBalance = new Money(savingsDTO.getMinimumBalanceAmount(),
                savingsDTO.getMinimumBalanceCurrency());
        BigDecimal interestRate = savingsDTO.getInterestRate();
        Long primaryOwnerId = savingsDTO.getPrimaryOwnerId();
        Optional<Long> optionalSecondaryOwnerId = savingsDTO.getSecondaryOwnerId();

        return accountService.createSavingsAccount(balance,secretKey,minimumBalance,interestRate,primaryOwnerId,
                optionalSecondaryOwnerId);
    }

    // Modify checking account data (admin)
    @PutMapping("/accounts/checkings/{id}/modify-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyCheckingAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long id, @RequestBody @Valid CheckingDTO checkingDTO){
        Money balance = checkingDTO.getBalance();
        String secretKey = checkingDTO.getSecretKey();
        Long primaryOwnerId = checkingDTO.getPrimaryOwnerId();
        Optional<Long> optionalSecondaryOwnerId = checkingDTO.getSecondaryOwnerId();

        accountService.modifyCheckingAccount(balance,secretKey,primaryOwnerId,optionalSecondaryOwnerId,id);
    }

    // Modify credit card account data (admin)
    @PutMapping("/accounts/credit-cards/{id}/modify-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyCreditCardAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long id, @RequestBody @Valid CreditCardDTO creditCardDTO){
        Money balance = creditCardDTO.getBalance();
        String secretKey = creditCardDTO.getSecretKey();
        Money creditLimit = new Money(creditCardDTO.getCreditLimitAmount(), creditCardDTO.getCreditLimitCurrency());
        BigDecimal interestRate = creditCardDTO.getInterestRate();
        Long primaryOwnerId = creditCardDTO.getPrimaryOwnerId();
        Optional<Long> optionalSecondaryOwnerId = creditCardDTO.getSecondaryOwnerId();

        accountService.modifyCreditCardAccount(balance,secretKey,creditLimit,interestRate,primaryOwnerId,
                optionalSecondaryOwnerId,id);
    }

    // Modify savings account data (admin)
    @PutMapping("/accounts/savings/{id}/modify-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifySavingsAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long id, @RequestBody @Valid SavingsDTO savingsDTO){
        Money balance = savingsDTO.getBalance();
        String secretKey = savingsDTO.getSecretKey();
        Money minimumBalance = new Money(savingsDTO.getMinimumBalanceAmount(), savingsDTO.getMinimumBalanceCurrency());
        BigDecimal interestRate = savingsDTO.getInterestRate();
        Long primaryOwnerId = savingsDTO.getPrimaryOwnerId();
        Optional<Long> optionalSecondaryOwnerId = savingsDTO.getSecondaryOwnerId();

        accountService.modifySavingsAccount(balance,secretKey,minimumBalance,interestRate,primaryOwnerId,
                optionalSecondaryOwnerId,id);
    }

    // Modify balance of an account (admin)
    @PatchMapping("/accounts/{id}/modify-balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyBalance(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long id, @RequestBody BalanceDTO balanceDTO){
        Money newBalance = balanceDTO.getNewBalance();

        accountService.modifyBalance(id,newBalance);
    }

    // Delete account by id (admin)
    @DeleteMapping("/delete/account/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long id) {
        Account account = accountService.findAccount(id);
        accountRepository.delete(account);
    }
}
