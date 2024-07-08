package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.ActionAgentProfitDto;
import rs.edu.raf.BankService.data.dto.TotalActionAgentProfitDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.enums.TransactionProfitType;

import java.util.List;

@Service
public interface ActionAgentProfitService {


    ActionAgentProfitDto createAgentProfit(Object securitiesTransaction, SecuritiesOwnership securitiesOwnership, int quantity);

    //    List<ActionAgentProfitDto> createAgentProfit(OrderTransaction orderTransaction);

    List<ActionAgentProfitDto> getAllProfits();

    List<TotalActionAgentProfitDto> getTotalProfitsByUsers();

    Double getAgentsTotalProfits();

}
