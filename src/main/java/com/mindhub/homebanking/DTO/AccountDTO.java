package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private String number;
    private AccountType type;

    private LocalDateTime createDate;
    private double balance;
    private long id;
    private Set<TransactionDTO> transactions = new HashSet<>();
    private boolean isActive;


    public AccountDTO(Account account){
        this.id = account.getId();
        this.isActive = account.isActive();
        this.number = account.getNumber();
        this.type = account.getType();
        this.createDate = account.getCreateDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getId() {
        return id;
    }

    public AccountType getType() {
        return type;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
