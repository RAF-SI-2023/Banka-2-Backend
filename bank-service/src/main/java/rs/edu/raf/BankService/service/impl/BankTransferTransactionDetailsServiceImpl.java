package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.BankTransferTransactionDetailsDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.BankTransferTransactionDetails;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;
import rs.edu.raf.BankService.mapper.BankTransferTransactionDetailsMapper;
import rs.edu.raf.BankService.repository.BankTransferTransactionDetailsRepository;
import rs.edu.raf.BankService.service.BankTransferTransactionDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankTransferTransactionDetailsServiceImpl implements BankTransferTransactionDetailsService {


    private final BankTransferTransactionDetailsRepository bankTransferTransactionDetailsRepository;
    private final BankTransferTransactionDetailsMapper bankTransferTransactionDetailsMapper;

    @Override
    public List<BankTransferTransactionDetailsDto> getAllBankExchangeRates() {

        List<BankTransferTransactionDetails> bankTransferTransactionDetails = bankTransferTransactionDetailsRepository.findAll();

        List<BankTransferTransactionDetailsDto> bankTransferTransactionDetailsDtos = bankTransferTransactionDetails.stream()
                .map(bankTransferTransactionDetailsMapper::convertToDto)
                .toList();

        return bankTransferTransactionDetailsDtos ;
    }

    @Override
    public Double getTotalProfit() {

        List<BankTransferTransactionDetails> bankTransferTransactionDetails = bankTransferTransactionDetailsRepository.findAll();

        if (!bankTransferTransactionDetails.isEmpty()) {
            double totalProfit = 0.0;
            for (BankTransferTransactionDetails bankTransferTransactionDetail : bankTransferTransactionDetails) {
                totalProfit += bankTransferTransactionDetail.getTotalProfit();
            }
            return totalProfit;
        }

     return 0.0;
    }

    @Override
    public BankTransferTransactionDetailsDto createBankTransferTransactionDetails(ExchangeTransferTransactionDetails exchangeTransferTransactionDetails) {

        BankTransferTransactionDetails bankTransferTransactionDetails = new BankTransferTransactionDetails();
        bankTransferTransactionDetails.setExchangeTransferTransactionDetails(exchangeTransferTransactionDetails);
        bankTransferTransactionDetails.setAmount(exchangeTransferTransactionDetails.getAmount());
        bankTransferTransactionDetails.setFee(200);
        bankTransferTransactionDetails.setBoughtCurrency(exchangeTransferTransactionDetails.getFromCurrency());
        bankTransferTransactionDetails.setSoldCurrency(exchangeTransferTransactionDetails.getToCurrency());
        bankTransferTransactionDetails.setAmount(exchangeTransferTransactionDetails.getAmount());

        double soldCurrency = Integer.parseInt(bankTransferTransactionDetails.getSoldCurrency().split(" ")[0]);
        double boughtCurrency = Integer.parseInt(bankTransferTransactionDetails.getBoughtCurrency().split(" ")[0]);
        double avg = (soldCurrency + boughtCurrency) / 2;
        double diff = Math.abs(soldCurrency - boughtCurrency);
        double totalProfit = diff / avg * 100;

        bankTransferTransactionDetails.setTotalProfit(totalProfit + bankTransferTransactionDetails.getFee());

        bankTransferTransactionDetailsRepository.save(bankTransferTransactionDetails);

        return bankTransferTransactionDetailsMapper.convertToDto(bankTransferTransactionDetails);
    }


}
