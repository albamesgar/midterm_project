package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.SavingsDTO;
import com.ironhack.midterm_project.controller.dto.TransferDTO;
import com.ironhack.midterm_project.controller.interfaces.TransactionController;
import com.ironhack.midterm_project.model.accounts.Account;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.repository.AccountRepository;
import com.ironhack.midterm_project.repository.TransactionRepository;
import com.ironhack.midterm_project.repository.UserRepository;
import com.ironhack.midterm_project.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class TransactionControllerImpl implements TransactionController {
    // See my transactions (account holder) -> GET
    // See all transactions (admin) -> GET ?????
    // Do third-party transaction (third-party) -> POST
    // Do account transaction (account holder) -> POST
    // Fees and interests ????

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @PostMapping("/my-accounts/{id}/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public String transferFoundings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long id,
                                    @RequestBody @Valid TransferDTO transferDTO){
        //Check if the Id of my account is correct
        Account sendingAccount = accountRepository.findMyAccountById(userDetails.getUser().getId(),id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sending account not found"));

        //Check if the receiving account data is correct
        Long receivingAccountId = transferDTO.getAccountId();
        String receivingAccountOwnerName = transferDTO.getOwnerName();
        Money amount = transferDTO.getAmount();
        Account receivingAccount = accountRepository.findById(receivingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiving account not found"));
        List<String> ownersList = List.of(receivingAccount.getPrimaryOwner().getUsername(),
                receivingAccount.getSecondaryOwner().getUsername());
        if (!ownersList.contains(receivingAccountOwnerName)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The receptor indicated is not an owner of the " +
                    "receiving account");
        }

        //Check if my account has sufficient funds
        Money myBalance = new Money(sendingAccount.getBalance().getAmount(),sendingAccount.getBalance().getCurrency());
        myBalance.decreaseAmount(amount);
        if (myBalance.getAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have not sufficient founds in the account " +
                    "to transfer the indicated amount");
        }

        //Do transaction
        sendingAccount.setBalance(myBalance);
        receivingAccount.getBalance().increaseAmount(amount);
        accountRepository.saveAll(List.of(sendingAccount,receivingAccount));

        return "Transfer done to " + receivingAccountOwnerName;
    }
}
