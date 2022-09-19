package com.mindhub.homebanking.DTO;

public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private int payments;
    private String destinationNumberAccount;

    public LoanApplicationDTO(long id, Double amount, int payments, String destinationNumberAccount) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.destinationNumberAccount = destinationNumberAccount;
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getDestinationNumberAccount() {
        return destinationNumberAccount;
    }
}
