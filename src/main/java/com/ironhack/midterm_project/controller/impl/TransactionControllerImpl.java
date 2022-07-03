package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.transactions.ThirdPartyTransactionDTO;
import com.ironhack.midterm_project.controller.dto.transactions.TransferDTO;
import com.ironhack.midterm_project.controller.interfaces.TransactionController;
import com.ironhack.midterm_project.security.CustomUserDetails;
import com.ironhack.midterm_project.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TransactionControllerImpl implements TransactionController {
    @Autowired
    private TransactionService transactionService;

    //Do a transfer from account (account-holder)
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

    //Do a refund to an account (anyone)
    @PostMapping("/third-party/{hashedKey}/refund")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String thirdPartyRefund(@PathVariable String hashedKey,
                                   @RequestBody @Valid ThirdPartyTransactionDTO thirdPartyTransactionDTO){
        Long receivingAccountId = thirdPartyTransactionDTO.getAccountId();
        String secretKey = thirdPartyTransactionDTO.getAccountSecretKey();
        Money amount = thirdPartyTransactionDTO.getAmount();

        return transactionService.thirdPartyRefund(hashedKey,receivingAccountId,secretKey,amount);
    }

    //Do a discharge to an account (anyone)
    @PostMapping("/third-party/{hashedKey}/discharge")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String thirdPartyDischarge(@PathVariable String hashedKey,
                                   @RequestBody @Valid ThirdPartyTransactionDTO thirdPartyTransactionDTO){
        Long sendingAccountId = thirdPartyTransactionDTO.getAccountId();
        String secretKey = thirdPartyTransactionDTO.getAccountSecretKey();
        Money amount = thirdPartyTransactionDTO.getAmount();

        return transactionService.thirdPartyDischarge(hashedKey,sendingAccountId,secretKey,amount);
    }
}
