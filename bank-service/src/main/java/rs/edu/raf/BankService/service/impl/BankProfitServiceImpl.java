package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.BankProfitDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.BankTransferTransactionDetails;
import rs.edu.raf.BankService.data.entities.profit.ActionAgentProfit;
import rs.edu.raf.BankService.data.entities.profit.BankProfit;
import rs.edu.raf.BankService.mapper.BankProfitMapper;
import rs.edu.raf.BankService.repository.ActionAgentProfitRepository;
import rs.edu.raf.BankService.repository.BankProfitRepository;
import rs.edu.raf.BankService.repository.BankTransferTransactionDetailsRepository;
import rs.edu.raf.BankService.service.BankProfitService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankProfitServiceImpl implements BankProfitService {


    private final ActionAgentProfitRepository actionAgentProfitRepository;
    private final BankTransferTransactionDetailsRepository bankTransferTransactionDetailsRepository;
    private final BankProfitRepository bankProfitRepository;
    private final BankProfitMapper bankProfitMapper;

    @Override
    public BankProfitDto getTotalProfit() {
        List<ActionAgentProfit> actionAgentProfits = actionAgentProfitRepository.findAll();
        List<BankTransferTransactionDetails> bankTransferTransactionDetails = bankTransferTransactionDetailsRepository.findAll();

        double totalActionAgentProfit = actionAgentProfits.stream()
                .mapToDouble(ActionAgentProfit::getProfit)
                .sum();

        double totalBankTransferProfit = bankTransferTransactionDetails.stream()
                .mapToDouble(BankTransferTransactionDetails::getTotalProfit)
                .sum();

        double totalProfit = totalActionAgentProfit + totalBankTransferProfit;

        Optional<BankProfit> bankProfitT = bankProfitRepository.findById(1L); // Assume you have a method to get the first BankProfit
        BankProfit bankProfit;
        bankProfit = bankProfitT.orElseGet(BankProfit::new);

        bankProfit.setProfit(totalProfit);

        bankProfitRepository.save(bankProfit);

        return bankProfitMapper.bankProfitToBankProfitDto(bankProfit);
    }
}
