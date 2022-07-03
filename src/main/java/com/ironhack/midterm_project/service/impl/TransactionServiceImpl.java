package com.ironhack.midterm_project.service.impl;

import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.enums.Status;
import com.ironhack.midterm_project.enums.TransactionType;
import com.ironhack.midterm_project.model.Transaction;
import com.ironhack.midterm_project.model.accounts.Account;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.repository.AccountRepository;
import com.ironhack.midterm_project.repository.ThirdPartyRepository;
import com.ironhack.midterm_project.repository.TransactionRepository;
import com.ironhack.midterm_project.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public Boolean fraudFound(Account account, Transaction transaction) {
        LocalDateTime now = LocalDateTime.now();
        if (account.getTransactionsDone().size() <= 2) {
            return false;
        }
        Transaction secondLastTransaction = (Transaction) account.getTransactionsDone().toArray()
                [account.getTransactionsDone().size()-2];
        LocalDateTime secondLastTransactionTime = secondLastTransaction.getTransactionDate();
        if (now.getDayOfYear() == secondLastTransactionTime.getDayOfYear() &&
                now.getHour() == secondLastTransactionTime.getHour() &&
                now.getMinute() == secondLastTransactionTime.getMinute() &&
                now.getSecond() == secondLastTransactionTime.getSecond()) {
            return true;
        }
        if (account.getCreationDate().compareTo(now.toLocalDate().minusDays(1)) < 0) {
            return false;
        }
        BigDecimal highestDailyTotalMarker = findHighestDailyTotal(account).multiply(BigDecimal.valueOf(1.5));
        BigDecimal transactionsIn24hrs = transactionTotalInLastDay(account);
        if (transactionsIn24hrs.compareTo(highestDailyTotalMarker) > 0) {
            return true;
        }
        return false;
    }

    public BigDecimal findHighestDailyTotal(Account account) {
        if (account.getTransactionsDone().size() < 1) {
            return BigDecimal.ZERO;
        }
        BigDecimal maxTransaction = BigDecimal.ZERO;
        BigDecimal dailyTotal = BigDecimal.ZERO;
        Transaction firstTransaction = (Transaction) account.getTransactionsDone().toArray()[0];
        LocalDate transactionDate = firstTransaction.getTransactionDate().toLocalDate();
        for (int i = 0; i < account.getTransactionsDone().size(); i++) {
            Transaction transaction = (Transaction) account.getTransactionsDone().toArray()[i];
            if (transaction.getTransactionDate().toLocalDate().equals(transactionDate)) {
                dailyTotal.add(transaction.getAmount().getAmount());
            }
            else {
                if (dailyTotal.compareTo(maxTransaction) > 0) {
                    maxTransaction = dailyTotal;
                }
                dailyTotal = BigDecimal.ZERO;
                transactionDate = transaction.getTransactionDate().toLocalDate();
            }
        }
        return maxTransaction;
    }

    public BigDecimal transactionTotalInLastDay(Account account) {
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeYesterday = timeNow.minusDays(1);
        BigDecimal transactionTotal = BigDecimal.ZERO;
        for (int i = 0; i < account.getTransactionsDone().size(); i++) {
            Transaction transaction = (Transaction) account.getTransactionsDone().toArray()[i];
            if (transaction.getTransactionDate().isBefore(timeNow) &&
                    transaction.getTransactionDate().isAfter(timeYesterday)) {
                transactionTotal.add(transaction.getAmount().getAmount());
            }
        }
        return transactionTotal;
    }

    public String transferFoundings(Long userId, Long receivingAccountId, String receivingAccountOwnerName,
                                    Money amount, Long id){
        //Check if the Id of my account is correct
        Account sendingAccount = accountRepository.findMyAccountById(userId,id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sending account not found"));
        //Check if my account is frozen
        if (sendingAccount.getStatus().equals(Status.FROZEN)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is frozen, " +
                    "you can not do any transaction");
        }

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
        //Check if the receiving account is frozen
        if (receivingAccount.getStatus().equals(Status.FROZEN)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The receiving account is frozen, no transactions" +
                    " can be done");
        }

        //Check if my account has sufficient funds
        Money myBalance = new Money(sendingAccount.getBalance().getAmount(),sendingAccount.getBalance().getCurrency());
        myBalance.decreaseAmount(amount);
        if (myBalance.getAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have not sufficient founds in the account " +
                    "to transfer the indicated amount");
        }

        Transaction transaction = new Transaction(LocalDateTime.now(),amount,sendingAccount,receivingAccount);

        //Check for fraud
        if (fraudFound(sendingAccount,transaction)){
            sendingAccount.setStatus(Status.FROZEN);
            accountRepository.save(sendingAccount);
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Your account has been frozen because fraud has " +
                    "been identified");
        }

        //Do transaction
        sendingAccount.setBalance(myBalance);
        sendingAccount.getTransactionsDone().add(transaction);
        Money receivingBalance = receivingAccount.getBalance();
        receivingBalance.increaseAmount(amount);
        receivingAccount.setBalance(receivingBalance);
        receivingAccount.getTransactionsReceived().add(transaction);

        accountRepository.saveAll(List.of(sendingAccount,receivingAccount));
        transactionRepository.save(transaction);

        return "Transfer done to " + receivingAccountOwnerName;
    }

    public String thirdPartyRefund(String hashedKey, Long receivingAccountId,String secretKey,Money amount){
        secretKey = UUID.nameUUIDFromBytes(secretKey.getBytes()).toString();
        hashedKey = UUID.nameUUIDFromBytes(hashedKey.getBytes()).toString();
        //Check if third-party exists
        ThirdParty thirdParty = thirdPartyRepository.findByHashedKey(hashedKey).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));

        //Check if the receiving account data is correct
        Account receivingAccount = accountRepository.findById(receivingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiving account not found"));
        if (!secretKey.equals(receivingAccount.getSecretKey())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The secret key indicated is not correct");
        }
        //Check if the receiving account is frozen
        if (receivingAccount.getStatus().equals(Status.FROZEN)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The requested account is frozen, no transactions" +
                    " can be done");
        }

        //Do transaction
        Transaction transaction = new Transaction(LocalDateTime.now(),amount,receivingAccount,thirdParty,
                TransactionType.REFUND);

        Money receivingBalance = receivingAccount.getBalance();
        receivingBalance.increaseAmount(amount);
        receivingAccount.setBalance(receivingBalance);
        receivingAccount.getTransactionsReceived().add(transaction);

        accountRepository.save(receivingAccount);
        transactionRepository.save(transaction);

        return "Refund done to account " + receivingAccount.getId();
    }

    public String thirdPartyDischarge(String hashedKey,Long sendingAccountId,String secretKey,Money amount){
        secretKey = UUID.nameUUIDFromBytes(secretKey.getBytes()).toString();
        hashedKey = UUID.nameUUIDFromBytes(hashedKey.getBytes()).toString();
        //Check if third-party exists
        ThirdParty thirdParty = thirdPartyRepository.findByHashedKey(hashedKey).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));

        //Check if the sending account data is correct
        Account sendingAccount = accountRepository.findById(sendingAccountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sending account not found"));
        if (!secretKey.equals(sendingAccount.getSecretKey())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The secret key indicated is not correct");
        }
        //Check if the sending account is frozen
        if (sendingAccount.getStatus().equals(Status.FROZEN)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The requested account is frozen, no transactions" +
                    " can be done");
        }

        //Do transaction
        Transaction transaction = new Transaction(LocalDateTime.now(),amount,sendingAccount,thirdParty,
                TransactionType.DISCHARGE);

        Money sendingBalance = sendingAccount.getBalance();
        sendingBalance.decreaseAmount(amount);
        sendingAccount.setBalance(sendingBalance);
        sendingAccount.getTransactionsReceived().add(transaction);

        accountRepository.save(sendingAccount);
        transactionRepository.save(transaction);

        return "Discharge done to account " + sendingAccount.getId();
    }
}
