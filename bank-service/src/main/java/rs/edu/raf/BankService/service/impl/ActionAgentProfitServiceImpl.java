package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.ActionAgentProfitDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.TotalActionAgentProfitDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.profit.ActionAgentProfit;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionProfitType;
import rs.edu.raf.BankService.data.enums.TransactionType;
import rs.edu.raf.BankService.mapper.ActionAgentProfitMapper;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.ActionAgentProfitRepository;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.service.ActionAgentProfitService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class ActionAgentProfitServiceImpl implements ActionAgentProfitService {

    private final  ActionAgentProfitRepository actionAgentProfitRepository;
    private final  ActionAgentProfitMapper actionAgentProfitMapper;
    private final CashAccountRepository cashAccountRepository;
    private final TransactionMapper transactionMapper;



    @Override
    public ActionAgentProfitDto createAgentProfit(Object transaction, SecuritiesOwnership securitiesOwnership, int quantity) {

        if (transaction instanceof SecuritiesTransaction) {
            SecuritiesTransaction securitiesTransaction = (SecuritiesTransaction) transaction;
            GenericTransactionDto genericTransactionDto = transactionMapper.toGenericTransactionDto(securitiesTransaction);

                ActionAgentProfit actionAgentProfit = new ActionAgentProfit();
                CashAccount cashAccount = securitiesTransaction.getSenderCashAccount();
                String email = cashAccount.getEmail();
                actionAgentProfit.setUserEmail(email);
                long id= Integer.parseInt(genericTransactionDto.getId());
                actionAgentProfit.setTransactionId(id);
                actionAgentProfit.setTransactionType(TransactionProfitType.SECURITIES);
                LocalDateTime createdAt = securitiesTransaction.getCreatedAt();
                long createdAtMillis = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli();
                actionAgentProfit.setCreatedAt(createdAtMillis);

                double avgPrice = securitiesOwnership.getAverageBuyingPrice() * quantity;

                actionAgentProfit.setProfit(securitiesTransaction.getAmount() - avgPrice);
                actionAgentProfitRepository.save(actionAgentProfit);
                return actionAgentProfitMapper.actionAgentProfitToActionAgentProfitDto(actionAgentProfit);


        } else if (transaction instanceof OrderTransaction) {
            OrderTransaction orderTransaction = (OrderTransaction) transaction;
            ActionAgentProfit actionAgentProfit = new ActionAgentProfit();
            String accountNumber = orderTransaction.getAccountNumber();
            CashAccount cashAccount = cashAccountRepository.findByAccountNumber(accountNumber);
            String email = cashAccount.getEmail();
            actionAgentProfit.setUserEmail(email);
            actionAgentProfit.setTransactionId(orderTransaction.getOrderId());
            actionAgentProfit.setTransactionType(TransactionProfitType.ORDER);
            actionAgentProfit.setCreatedAt(System.currentTimeMillis());

            double avgPrice = securitiesOwnership.getAverageBuyingPrice() * quantity;
            actionAgentProfit.setProfit(orderTransaction.getPayoffAmount() - avgPrice);
            actionAgentProfitRepository.save(actionAgentProfit);
            return actionAgentProfitMapper.actionAgentProfitToActionAgentProfitDto(actionAgentProfit);

        }
        else {
            throw new IllegalArgumentException("Unsupported transaction type");
        }

    }

    @Override
    public List<ActionAgentProfitDto> getAllProfits() {

        List<ActionAgentProfit> actionAgentProfits = actionAgentProfitRepository.findAll();

        return actionAgentProfits.stream().map(actionAgentProfitMapper::actionAgentProfitToActionAgentProfitDto).toList();
    }

    @Override
    public List<TotalActionAgentProfitDto> getTotalProfitsByUsers() {
        List<ActionAgentProfit> actionAgentProfits = actionAgentProfitRepository.findAll();

        // Group by userEmail and sum the profits
        Map<String, Double> totalProfitsByUser = actionAgentProfits.stream()
                .collect(Collectors.groupingBy(
                        ActionAgentProfit::getUserEmail,
                        Collectors.summingDouble(ActionAgentProfit::getProfit)
                ));

        // Convert the map to a list of TotalActionAgentProfitDto
        return totalProfitsByUser.entrySet().stream()
                .map(entry -> new TotalActionAgentProfitDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


}
