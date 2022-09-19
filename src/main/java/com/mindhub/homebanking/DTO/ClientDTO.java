package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private String name, lastName, email;
    private long id;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> loans = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();


    public ClientDTO(Client client){
    this.id = client.getId();
    this.name = client.getName();
    this.lastName = client.getLastName();
    this.email = client.getEmail();
    this.accounts = client.getAccounts().stream().filter(Account::isActive).map(AccountDTO::new).collect(Collectors.toSet());
    this.loans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
    this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
}

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}

