package com.mindhub.homebanking;

import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.hibernate.sql.ordering.antlr.GeneratedOrderByLexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientService clientService, AccountService accountService, TransactionService transactionService, LoanService loanService, ClientLoanService clientLoanService, CardService cardService){
		return (args) -> {


			Client client = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));
			Client client1 = new Client("Alu", "Romero", "alu@gmail.com", passwordEncoder.encode("alu123"));
  			Client admin = new Client("ADMIN", "ISTRADOR", "admin@admin.com", passwordEncoder.encode("admin123"));

			Account account1 = new Account("VIN00000001", LocalDateTime.now(), 5000.0, client, AccountType.AHORRO);
			Account account2 = new Account("VIN00000002", LocalDateTime.now().plusDays(1), 7500.0, client, AccountType.CORRIENTE);
			Account account3 =new Account("VIN00000003", LocalDateTime.now().minusDays(1), 5000000.0, client1, AccountType.AHORRO);

			Transaction transaction1 = new Transaction("Varios",-2000.0, LocalDateTime.now().minusDays(3), account1, TransactionType.DEBIT);
			Transaction transaction2 = new Transaction("Alquiler", 5000, LocalDateTime.now().minusDays(8), account2, TransactionType.CREDIT);
			Transaction transaction3 = new Transaction("Varios",-8000.0, LocalDateTime.now().minusDays(5), account2, TransactionType.DEBIT);
			Transaction transaction4 = new Transaction("Expensas",12000.0, LocalDateTime.now().minusDays(2), account1, TransactionType.CREDIT);
			Transaction transaction5 = new Transaction("Varios",-6000.0, LocalDateTime.now().minusDays(1), account1, TransactionType.DEBIT);
			Transaction transaction6 = new Transaction("Varios",7000.0, LocalDateTime.now(), account1, TransactionType.CREDIT);
            Transaction transaction7 = new Transaction("Varios",9000.0, LocalDateTime.now().minusDays(7), account3, TransactionType.CREDIT);
            Transaction transaction8 = new Transaction("Varios",-2000.0, LocalDateTime.now().minusDays(5), account3, TransactionType.DEBIT);
            Transaction transaction9 = new Transaction("Varios",10000.0, LocalDateTime.now().minusDays(3), account3, TransactionType.CREDIT);
            Transaction transaction10 = new Transaction("Varios",4000.0, LocalDateTime.now().minusDays(2), account3, TransactionType.CREDIT);
            Transaction transaction11 = new Transaction("Varios",-200.0, LocalDateTime.now().minusDays(1), account3, TransactionType.DEBIT);
            Transaction transaction12 = new Transaction("Varios",99000.0, LocalDateTime.now(), account3, TransactionType.CREDIT);

            Loan loan1 = new Loan("Hipotecario", 500000.00, List.of(12, 24, 36, 48, 60), "./assets/img/house.png", 0.2);
			Loan loan2 = new Loan("Personal", 100000.00, List.of(6, 12, 24), "./assets/img/woman.png", 0.1);
			Loan loan3 = new Loan("Automotriz", 300000.00, List.of(6, 12, 24, 36), "./assets/img/car.png", 0.15);

			ClientLoan clientLoan = new ClientLoan(400000.00, 60, client, loan1);
			ClientLoan clientLoan1 = new ClientLoan(50000.00, 12, client, loan2);
			ClientLoan clientLoan2 = new ClientLoan(100000.00, 24, client1, loan2);
			ClientLoan clientLoan3 = new ClientLoan(200000.00, 36, client1, loan3);

			Card card1 = new Card("1111-2222-3333-4444", CardType.DEBIT, CardColor.GOLD, 567, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client);
			Card card2 = new Card("1112-2223-3334-4445", CardType.CREDIT, CardColor.TITANIUM, 789, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client);
			Card card3 = new Card("1113-2224-3335-4446", CardType.CREDIT, CardColor.SILVER, 987, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1);

			clientService.saveClient(client);
			clientService.saveClient(client1);
			clientService.saveClient(admin);

			accountService.saveAccount(account1);
			accountService.saveAccount(account2);
			accountService.saveAccount(account3);

			transactionService.saveTransaction(transaction1);
			transactionService.saveTransaction(transaction2);
			transactionService.saveTransaction(transaction3);
			transactionService.saveTransaction(transaction4);
			transactionService.saveTransaction(transaction5);
			transactionService.saveTransaction(transaction6);
			transactionService.saveTransaction(transaction7);
			transactionService.saveTransaction(transaction8);
			transactionService.saveTransaction(transaction9);
			transactionService.saveTransaction(transaction10);
			transactionService.saveTransaction(transaction11);
			transactionService.saveTransaction(transaction12);

			loanService.saveLoan(loan1);
			loanService.saveLoan(loan2);
			loanService.saveLoan(loan3);

			clientLoanService.saveClientLoan(clientLoan);
			clientLoanService.saveClientLoan(clientLoan1);
			clientLoanService.saveClientLoan(clientLoan2);
			clientLoanService.saveClientLoan(clientLoan3);

			cardService.saveCard(card1);
			cardService.saveCard(card2);
			cardService.saveCard(card3);

		};
	}
}
