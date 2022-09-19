package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static com.mindhub.homebanking.Utils.Utils.getRandomNumber;

@RestController
@RequestMapping("/api")
public class AccountControllers {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;


    /*GET ALL ACCOUNTS*/
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAllAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    /*GET ACCOUNT BY ID*/
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.getAccountById(id));
    }

    /*CREATE A NEW ACCOUNT*/
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType type) {

        int random = getRandomNumber(11111111, 99999999);
        Client client = clientService.getClientByEmail(authentication.getName());
        ClientDTO clientDTO = new ClientDTO(client);

        if(client == null){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(clientDTO.getAccounts().toArray().length >= 3){
            return new ResponseEntity<>("Max accounts", HttpStatus.FORBIDDEN);
        }

        accountService.saveAccount(new Account("VIN" + random, LocalDateTime.now(),00.00, client, type));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*DEACTIVATE AN ACCOUNT*/
    @PatchMapping("/accounts/delete")
    public ResponseEntity<Object> deleteAccount(Authentication authentication,@RequestParam String accountNumber){
        Client client = clientService.getClientByEmail(authentication.getName());
        Account account = accountService.getAccountByNumber(accountNumber);
        if (client == null){
            return new ResponseEntity<>("Client doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("Account doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() != 0 ){
            return new ResponseEntity<>("The account has balance", HttpStatus.FORBIDDEN);
        }
        account.setActive(false);
        accountService.saveAccount(account);
        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }



}
