package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.OtcOfferDto;

@Service
public interface OtcBankTransactionService {
    GenericTransactionDto buyStock(OtcOfferDto otcOfferDto);
    GenericTransactionDto sellStock(OtcOfferDto otcOfferDto);
}
