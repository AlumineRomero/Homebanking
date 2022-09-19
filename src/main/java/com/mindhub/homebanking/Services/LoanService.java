package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    public List<Loan> getAllLoans();
    public Loan getLoanById(Long id);
    public void saveLoan(Loan loan);

}
