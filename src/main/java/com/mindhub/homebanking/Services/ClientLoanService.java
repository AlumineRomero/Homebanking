package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;

public interface ClientLoanService {
    public ClientLoan getClientLoanById(Long id);
    public void saveClientLoan(ClientLoan clientLoan);

}
