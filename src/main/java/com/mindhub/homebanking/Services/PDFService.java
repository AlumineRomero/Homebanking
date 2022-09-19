package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PDFService {
    public void generatePDF(HttpServletResponse response, List<Transaction> transactions, Account account);
}
