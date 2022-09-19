package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private String cardHolder, number;
    private CardType type;
    private CardColor color;
    private Integer cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private boolean isActive;


    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.type = card.getType();
        this.color = card.getColor();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.isActive = card.isActive();

    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public Integer getCvv() {
        return cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

}
