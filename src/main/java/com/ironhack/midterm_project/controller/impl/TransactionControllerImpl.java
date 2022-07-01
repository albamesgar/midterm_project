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
import com.ironhack.midterm_project.service.interfaces.TransactionService;
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

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/my-accounts/{id}/transfer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String transferFoundings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long id,
                                    @RequestBody @Valid TransferDTO transferDTO){
        Long userId = userDetails.getUser().getId();
        Long receivingAccountId = transferDTO.getAccountId();
        String receivingAccountOwnerName = transferDTO.getOwnerName();
        Money amount = transferDTO.getAmount();

         return transactionService.transferFoundings(userId,receivingAccountId,receivingAccountOwnerName,
                 amount,id);
    }

    @PostMapping("/third-party/{hashedKey}/refund")
    public String thirdPartyRefund(@PathVariable String hashedKey,
                                   @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO){
        Long receivingAccountId = thirdPartyTransactionDTO.getAccountId();
        String secretKey = thirdPartyTransactionDTO.getAccountSecretKey();
        Money amount = thirdPartyTransactionDTO.getAmount();

        return transactionService.thirdPartyRefund(hashedKey,receivingAccountId,secretKey,amount);
    }

    @PostMapping("/third-party/{hashedKey}/discharge")
    public String thirdPartyDischarge(@PathVariable String hashedKey,
                                   @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO){
        Long sendingAccountId = thirdPartyTransactionDTO.getAccountId();
        String secretKey = thirdPartyTransactionDTO.getAccountSecretKey();
        Money amount = thirdPartyTransactionDTO.getAmount();

        return transactionService.thirdPartyDischarge(hashedKey,sendingAccountId,secretKey,amount);
    }
}
