package rs.edu.raf.BankService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.bootstrap.exchangeRatesUtils.ExchangeRateApiResponse;
import rs.edu.raf.BankService.bootstrap.exchangeRatesUtils.ExchangeRateBootstrapUtil;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.data.entities.credit.Credit;
import rs.edu.raf.BankService.data.entities.credit.CreditRequest;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.data.enums.*;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.CardRepository;
import rs.edu.raf.BankService.repository.ExchangeRateRepository;
import rs.edu.raf.BankService.repository.credit.CreditRepository;
import rs.edu.raf.BankService.repository.credit.CreditRequestRepository;

import java.util.*;

@Component
@RequiredArgsConstructor
@Profile("test")
public class BootstrapTestData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapTestData.class);
    private final AccountRepository accountRepository;
    private final CreditRepository creditRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final CardRepository cardRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public void run(String... args) throws Exception {

        Card card1 = new Card();
        card1.setAccountNumber("0004444999999999");
        card1.setCvvCode("133");
        card1.setIdentificationCardNumber(1000000000000000L);
        card1.setCardType(CardType.CREDIT);
        card1.setCreationDate(1712602820L);
        card1.setExpirationDate(1775682066000L);
        card1.setLimitCard(1111000L);
        card1.setStatus(true);
        card1.setNameOfCard("TEST");

        addCardIfIdentificationCardNumberIsNotPresent(card1);


    }

    private void addCardIfIdentificationCardNumberIsNotPresent(Card card){
        if(cardRepository.findByIdentificationCardNumber(card.getIdentificationCardNumber()).isEmpty()){
            cardRepository.save(card);
        }
    }
}
