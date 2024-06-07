package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.BankTransferTransactionDetailsDto;
import rs.edu.raf.BankService.data.dto.ExchangeRatesDto;
import rs.edu.raf.BankService.data.dto.ExchangeRequestDto;
import rs.edu.raf.BankService.data.dto.ExchangeTransferDetailsDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;

import java.util.List;

@Service
public interface BankTransferTransactionDetailsService {


    List<BankTransferTransactionDetailsDto> getAllBankExchangeRates();

    Double getTotalProfit();


    BankTransferTransactionDetailsDto createBankTransferTransactionDetails(ExchangeTransferTransactionDetails exchangeTransferTransactionDetails);

}
