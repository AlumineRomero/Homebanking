package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.LoanApplicationDTO;
import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class LoanControllers {
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;


    /*GET A LIST OF ALL LOANS*/
    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getAllLoans().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    /*GET A LOAN BY ID*/
    @GetMapping("/loans/{id}")
    public LoanDTO getLoan(@PathVariable Long id){
        return new LoanDTO(loanService.getLoanById(id));
    }

    /*CREATE A CLIENTLOAN*/
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applicationForLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplication){
        Client client = clientService.getClientByEmail(authentication.getName());
        Loan loan = loanService.getLoanById(loanApplication.getId());
        Account destinationAccount = accountService.getAccountByNumber(loanApplication.getDestinationNumberAccount());
        ClientLoan clientLoan = new ClientLoan((loanApplication.getAmount() * loan.getInterest()) + loanApplication.getAmount(), loanApplication.getPayments(), client, loan);
        List<String> loansName = client.getClientLoans().stream().map(clientLoan1 -> clientLoan1.getLoan().getName()).collect(Collectors.toList());

        if (loanApplication.getPayments() == 0 || loanApplication.getPayments() == 0 ) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }
        if (loan == null) {
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Excess amount", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplication.getPayments())) {
            return new ResponseEntity<>("Wrong Payment", HttpStatus.FORBIDDEN);
        }
        if (destinationAccount == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if(loansName.contains(clientLoan.getLoan().getName())){
            return new ResponseEntity<>("Already apply for this Loan", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(destinationAccount)) {
            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }
        clientLoanService.saveClientLoan(clientLoan);
        Transaction transaction = new Transaction(loan.getName() + " Prestamo aprobado", loanApplication.getAmount() , LocalDateTime.now(), destinationAccount, TransactionType.CREDIT);
        transactionService.saveTransaction(transaction);
        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplication.getAmount());

        return new ResponseEntity<>("Approved loan", HttpStatus.CREATED);

    }

}
