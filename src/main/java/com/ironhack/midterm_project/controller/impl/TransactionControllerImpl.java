package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.SavingsDTO;
import com.ironhack.midterm_project.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.midterm_project.controller.dto.TransferDTO;
import com.ironhack.midterm_project.controller.interfaces.TransactionController;
import com.ironhack.midterm_project.enums.TransactionType;
import com.ironhack.midterm_project.model.Transaction;
import com.ironhack.midterm_project.model.accounts.Account;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.repository.AccountRepository;
import com.ironhack.midterm_project.repository.ThirdPartyRepository;
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
import java.time.LocalDateTime;
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
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @PostMapping("/my-accounts/{id}/transfer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String transferFoundings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long id,
                                    @RequestBody @Valid TransferDTO transferDTO){
        //Check if the Id of my account is correct
        Account sendingAccount = accountRepository.findMyAccountById(userDetails.getUser().getId(),id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sending account not found"));

        //Check if the receiving account data is correct
        Long receivingAccountId = transferDTO.getAccountId();
        String receivingAccountOwnerName = transferDTO.getOwnerName();
        Account receivingAccount = accountRepository.findById(receivingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiving account not found"));
        List<String> ownersList = List.of(receivingAccount.getPrimaryOwner().getUsername(),
                receivingAccount.getSecondaryOwner().getUsername());
        if (!ownersList.contains(receivingAccountOwnerName)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The receptor indicated is not an owner of the " +
                    "receiving account");
        }

        //Check if my account has sufficient funds
        Money amount = transferDTO.getAmount();
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

        Transaction transaction = new Transaction(LocalDateTime.now(),amount,sendingAccount,receivingAccount);
        transactionRepository.save(transaction);

        return "Transfer done to " + receivingAccountOwnerName;
    }

    @PostMapping("/third-party/{hashedKey}/refund")
    public String thirdPartyRefund(@PathVariable String hashedKey,
                                   @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO){
        //Check if third-party exists
        ThirdParty thirdParty = thirdPartyRepository.findByHashedKey(hashedKey).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));

        //Check if the receiving account data is correct
        Long receivingAccountId = thirdPartyTransactionDTO.getAccountId();
        String secretKey = thirdPartyTransactionDTO.getAccountSecretKey();
        Account receivingAccount = accountRepository.findById(receivingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiving account not found"));
        if (!secretKey.equals(receivingAccount.getSecretKey())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The secret key indicated is not correct");
        }

        //Do transaction
        Money amount = thirdPartyTransactionDTO.getAmount();
        receivingAccount.getBalance().increaseAmount(amount);
        accountRepository.save(receivingAccount);

        Transaction transaction = new Transaction(LocalDateTime.now(),amount,receivingAccount,thirdParty,
                TransactionType.REFUND);
        transactionRepository.save(transaction);

        return "Refund done to account " + receivingAccount.getId();
    }

    @PostMapping("/third-party/{hashedKey}/discharge")
    public String thirdPartyDischarge(@PathVariable String hashedKey,
                                   @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO){
        //Check if third-party exists
        ThirdParty thirdParty = thirdPartyRepository.findByHashedKey(hashedKey).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));

        //Check if the sending account data is correct
        Long sendingAccountId = thirdPartyTransactionDTO.getAccountId();
        String secretKey = thirdPartyTransactionDTO.getAccountSecretKey();
        Account sendingAccount = accountRepository.findById(sendingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sending account not found"));
        if (!secretKey.equals(sendingAccount.getSecretKey())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The secret key indicated is not correct");
        }

        //Do transaction
        Money amount = thirdPartyTransactionDTO.getAmount();
        sendingAccount.getBalance().decreaseAmount(amount);
        accountRepository.save(sendingAccount);

        Transaction transaction = new Transaction(LocalDateTime.now(),amount,sendingAccount,thirdParty,
                TransactionType.DISCHARGE);
        transactionRepository.save(transaction);

        return "Discharge done to account " + sendingAccount.getId();
    }
}
