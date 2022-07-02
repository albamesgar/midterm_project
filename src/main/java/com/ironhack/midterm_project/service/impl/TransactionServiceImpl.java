package com.ironhack.midterm_project.service.impl;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.enums.TransactionType;
import com.ironhack.midterm_project.model.Transaction;
import com.ironhack.midterm_project.model.accounts.Account;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.repository.AccountRepository;
import com.ironhack.midterm_project.repository.ThirdPartyRepository;
import com.ironhack.midterm_project.repository.TransactionRepository;
import com.ironhack.midterm_project.service.interfaces.TransactionService;
import com.ironhack.midterm_project.utils.PasswordEncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String transferFoundings(Long userId, Long receivingAccountId, String receivingAccountOwnerName,
                                    Money amount, Long id){
        //Check if the Id of my account is correct
        Account sendingAccount = accountRepository.findMyAccountById(userId,id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sending account not found"));

        //Check if the receiving account data is correct
        Account receivingAccount = accountRepository.findById(receivingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiving account not found"));
        List<String> ownersList = List.of(receivingAccount.getPrimaryOwner().getUsername());
        if (receivingAccount.getSecondaryOwner()!=null){
            ownersList = List.of(receivingAccount.getPrimaryOwner().getUsername(),
                    receivingAccount.getSecondaryOwner().getUsername());
        }
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
        Money receivingBalance = receivingAccount.getBalance();
        receivingBalance.increaseAmount(amount);
        receivingAccount.setBalance(receivingBalance);
        accountRepository.saveAll(List.of(sendingAccount,receivingAccount));

        Transaction transaction = new Transaction(LocalDateTime.now(),amount,sendingAccount,receivingAccount);
        transactionRepository.save(transaction);

        return "Transfer done to " + receivingAccountOwnerName;
    }

    public String thirdPartyRefund(String hashedKey, Long receivingAccountId,String secretKey,Money amount){
        hashedKey = passwordEncoder.encode(hashedKey);
        secretKey = passwordEncoder.encode(secretKey);

        //Check if third-party exists
        ThirdParty thirdParty = thirdPartyRepository.findByHashedKey(hashedKey).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));

        //Check if the receiving account data is correct
        Account receivingAccount = accountRepository.findById(receivingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiving account not found"));
        if (!secretKey.equals(receivingAccount.getSecretKey())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The secret key indicated is not correct");
        }

        //Do transaction
        Money receivingBalance = receivingAccount.getBalance();
        receivingBalance.increaseAmount(amount);
        receivingAccount.setBalance(receivingBalance);
        accountRepository.save(receivingAccount);

        Transaction transaction = new Transaction(LocalDateTime.now(),amount,receivingAccount,thirdParty,
                TransactionType.REFUND);
        transactionRepository.save(transaction);

        return "Refund done to account " + receivingAccount.getId();
    }

    public String thirdPartyDischarge(String hashedKey,Long sendingAccountId,String secretKey,Money amount){
        hashedKey = passwordEncoder.encode(hashedKey);
        secretKey = passwordEncoder.encode(secretKey);

        //Check if third-party exists
        ThirdParty thirdParty = thirdPartyRepository.findByHashedKey(hashedKey).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));

        //Check if the sending account data is correct
        Account sendingAccount = accountRepository.findById(sendingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sending account not found"));
        if (!secretKey.equals(sendingAccount.getSecretKey())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The secret key indicated is not correct");
        }

        //Do transaction
        sendingAccount.getBalance().decreaseAmount(amount);
        accountRepository.save(sendingAccount);

        Transaction transaction = new Transaction(LocalDateTime.now(),amount,sendingAccount,thirdParty,
                TransactionType.DISCHARGE);
        transactionRepository.save(transaction);

        return "Discharge done to account " + sendingAccount.getId();
    }
}