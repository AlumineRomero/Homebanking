package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    public List<Client> getAllClients();
    public Client getClientByEmail(String email);
    public Client getClientByAccountsNumber(String number);
    public Client getClientById(Long id);
    public void saveClient(Client client);

}
