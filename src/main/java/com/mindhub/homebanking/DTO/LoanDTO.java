package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Loan;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.util.ArrayList;
import java.util.List;

public class LoanDTO {
    private long id;
    private String name, image;
    private double maxAmount, interest;
    private List<Integer> payments = new ArrayList<>();


    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.image = loan.getImage();
        this.interest = loan.getInterest();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public String getImage() {
        return image;
    }

    public double getInterest() {
        return interest;
    }
}
