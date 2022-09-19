package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.DTO.TransactionDTO;
import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.PDFService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.PDFGenerator.generatePDF;

@RestController
@RequestMapping("/api")
public class TransactionControllers {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PDFService pdfService;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionService.getAllTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }


/*POST TO MAKE TRANSACTIONS*/
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount, @RequestParam String description, @RequestParam String sourceNumber, @RequestParam String destinationNumber, Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        Account sourceAccount = accountService.getAccountByNumber(sourceNumber);
        Account destinationAccount = accountService.getAccountByNumber(destinationNumber);

        if (client == null){
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty() || sourceNumber.isEmpty() || destinationNumber.isEmpty() || amount == 0.0) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }
        if (sourceNumber.equals(destinationNumber)){
            return new ResponseEntity<>("Same Account", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount == null){
            return new ResponseEntity<>("Source account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(sourceAccount)){
            return new ResponseEntity<>("The account doesn't belong to this client ", HttpStatus.FORBIDDEN);
        }
        if (destinationAccount == null){
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount.getBalance() <= amount){
            return new ResponseEntity<>("Not enough amount", HttpStatus.FORBIDDEN);
        }
            Transaction transaction1 = new Transaction(description, amount * -1, LocalDateTime.now(), sourceAccount, TransactionType.DEBIT);
            Transaction transaction2 = new Transaction(description, amount , LocalDateTime.now(), destinationAccount, TransactionType.CREDIT);
            transactionService.saveTransaction(transaction1);
            transactionService.saveTransaction(transaction2);
            sourceAccount.setBalance(sourceAccount.getBalance() - amount);
            destinationAccount.setBalance(destinationAccount.getBalance() + amount);

            return new ResponseEntity<>( HttpStatus.CREATED);
        }


    /*GET CLIENT BY ACCOUNT NUMBER*/
    @GetMapping("/transactions/{destinationNumber}")
    public ClientDTO getClient(@PathVariable String destinationNumber){
        Client client = clientService.getClientByAccountsNumber(destinationNumber);
        Client client1 = new Client(client.getName(), client.getLastName(), client.getEmail());
        ClientDTO clientDTO = new ClientDTO(client1);
        return clientDTO;
    }

    /*GET TRANSACTIONS HISTORY*/
    @GetMapping("/transactions/current")
    public ResponseEntity<Object> getTransactionsCurrent(HttpServletResponse response, Authentication authentication, @RequestParam String accountNumber, @RequestParam(required = false) String start, @RequestParam(required = false) String end){
        Client client = clientService.getClientByEmail(authentication.getName());
        Account account = accountService.getAccountByNumber(accountNumber);
        List<Transaction> transactions;
        if (client == null){
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Account doesn't belong to this client", HttpStatus.FORBIDDEN);
        }
        if (!(start.isEmpty() || end.isEmpty())){
            LocalDateTime startDate = LocalDateTime.parse(start);
            LocalDateTime endDate = LocalDateTime.parse(end);
            transactions = transactionService.getTransactionsByAccountAndDate(account,startDate,endDate);
        } else {
            transactions = transactionService.getAllTransactionsByAccount(account);
        }
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=MontBank-ResumenDeCuenta.pdf";
        response.setHeader(headerKey, headerValue);
        pdfService.generatePDF(response, transactions, account);
        return  new ResponseEntity<>("", HttpStatus.ACCEPTED);
        }



}


