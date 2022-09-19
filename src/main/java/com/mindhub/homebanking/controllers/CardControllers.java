package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.CardDTO;
import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.models.*;
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
public class CardControllers {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    /*GET A LIST OF ALL CARDS*/
    @GetMapping("/cards")
    public List<CardDTO> getCards(){
        return cardService.getAllCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    /*GET A CARD BY ID*/
    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return new CardDTO(cardService.getCardById(id));
    }

    /*CREATE CARDS*/
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardColor color, @RequestParam CardType type) {
        int random1 = getRandomNumber(1111, 9999);
        int random2 = getRandomNumber(1111, 9999);
        int random3 = getRandomNumber(1111, 9999);
        int random4 = getRandomNumber(1111, 9999);
        int cvv = getRandomNumber(111, 999);

        Client client = clientService.getClientByEmail(authentication.getName());

        if(client == null){
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(client.getCards().stream().filter(card -> card.getType() == type).collect(Collectors.toList()).size() >= 3){
            return new ResponseEntity<>("Max cards", HttpStatus.FORBIDDEN);
        }

            Card card = new Card(random1 + "-" + random2 + "-" + random3 + "-" + random4, type, color, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client);
            cardService.saveCard(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

    /*ACTIVATE OR DEACTIVATE CARDS*/
    @PatchMapping("/cards/state")
    public ResponseEntity<Object> changeIsActive(Authentication authentication,@RequestParam String number, @RequestParam String state){
        Client client = clientService.getClientByEmail(authentication.getName());
        Card card = cardService.getCardByNumber(number);
        if (client == null){
            return new ResponseEntity<>("Client doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if (card == null){
            return new ResponseEntity<>("Card doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if(state.equals("deactivate")){
            card.setActive(false);
        } else{
            card.setActive(true);
        }
        cardService.saveCard(card);
        return new ResponseEntity<>("Change isActive property success", HttpStatus.ACCEPTED);
    }

}
