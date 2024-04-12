package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRates, Long> {

    ExchangeRates findByFromCurrencyAndToCurrency(String currencyCode, String currencyCode1);

}
