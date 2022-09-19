package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.Utils.getRandomNumber;

@RestController
@RequestMapping("/api")
public class ClientControllers {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /*GET ALL CLIENTS*/
    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
    return clientService.getAllClients().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    /*GET A CLIENT BY ID*/
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientService.getClientById(id));
    }


    /*REGISTER A CLIENT*/
    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String name, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        int random = getRandomNumber(11111111, 99999999);

        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientService.getClientByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(name, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        accountService.saveAccount(new Account("VIN" + random, LocalDateTime.now(),00.00, client, AccountType.AHORRO));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*GET DATA FROM AUTHENTICATED CLIENT*/
    @GetMapping("/clients/current")
    public ClientDTO  getAll(Authentication authentication) {
        Client client = clientService.getClientByEmail(authentication.getName());
        return new ClientDTO(client);
    }


}
