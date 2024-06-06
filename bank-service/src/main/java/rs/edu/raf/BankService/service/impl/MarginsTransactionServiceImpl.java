package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.BankService.data.dto.BankStockDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionResponseDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.mapper.MarginsTransactionMapper;
import rs.edu.raf.BankService.repository.MarginsAccountRepository;
import rs.edu.raf.BankService.repository.MarginsTransactionRepository;
import rs.edu.raf.BankService.repository.specification.MarginsTransactionSpecification;
import rs.edu.raf.BankService.service.MarginsTransactionService;
import rs.edu.raf.BankService.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MarginsTransactionServiceImpl implements MarginsTransactionService {

    private final MarginsAccountRepository marginsAccountRepository;
    private final MarginsTransactionRepository marginsTransactionRepository;
    private final MarginsTransactionMapper transactionMapper;
    private final RestTemplate restTemplate;
    private final OrderService orderService;

    private final static String STOCK_SERVICE_URL = "http://localhost:8001/api";
    private final static String PRICE_ENDPOINT = "/bank-stock/listing-price";

    @Override
    public List<MarginsTransactionResponseDto> getTransactions(String currencyCode, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<MarginsTransaction> spec = Specification
                .where(MarginsTransactionSpecification.hasCurrency(currencyCode))
                .and(MarginsTransactionSpecification.isBetweenDates(startDate, endDate));

        return marginsTransactionRepository
                .findAll(spec)
                .stream().map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MarginsTransactionResponseDto createTransaction(MarginsTransactionRequestDto marginsTransactionRequestDto) {
        Order order = orderService.findById(marginsTransactionRequestDto.getOrderId());

        Double listPricePerUnit = getListingPrice(order.getListingId(), order.getListingType());
        Double orderPrice = order.getQuantity() * listPricePerUnit;
        Double initialMargin =  marginsTransactionRequestDto.getInitialMargin();
        Double loanValue = orderPrice - initialMargin;
        Double interest = loanValue * 0.05;

        MarginsTransaction transaction = transactionMapper.toEntity(marginsTransactionRequestDto);
        transaction.setOrderPrice(orderPrice);
        transaction.setLoanValue(loanValue);
        transaction.setInterest(interest);
        transaction.setInvestmentAmount(initialMargin);
        // popraviti da koristi id iz security context-a tj. jwt (ili ostaviti ovako je lakse)
        transaction.setUserId(marginsTransactionRequestDto.getUserId());
        transaction.setFallbackValues(order.getListingType());

        MarginsAccount updatedMarginsAccount = updateMarginsAccount(marginsTransactionRequestDto.getMarginsAccountId(), transaction);
        transaction.setMarginsAccount(updatedMarginsAccount);

        MarginsTransaction savedTransaction = marginsTransactionRepository.save(transaction);

        return transactionMapper.toDto(savedTransaction);
    }

    private MarginsAccount updateMarginsAccount(Long marginsAccountId, MarginsTransaction transaction) {
        MarginsAccount marginsAccount = marginsAccountRepository.findById(marginsAccountId)
                .orElseThrow(() -> new RuntimeException("Margin account not found"));

        if (transaction.isTransactionDeposit()) {
            marginsAccount.setBalance(marginsAccount.getBalance() - transaction.getInvestmentAmount());
            marginsAccount.setLoanValue(marginsAccount.getLoanValue() + transaction.getLoanValue());
            marginsAccount.setMaintenanceMargin(
                    marginsAccount.getMaintenanceMargin() + transaction.getMaintenanceMargin());
        }
        else {
            marginsAccount.setBalance(marginsAccount.getBalance() + transaction.getInvestmentAmount());
            marginsAccount.setMaintenanceMargin(
                    marginsAccount.getMaintenanceMargin() - transaction.getMaintenanceMargin());
        }

        marginsAccount.setFallbackValues();
        marginsAccount.addTransaction(transaction);
        return marginsAccountRepository.save(marginsAccount);
    }

    private Double getListingPrice(Long listingId, ListingType listingType) {
        String url = STOCK_SERVICE_URL + PRICE_ENDPOINT;
        BankStockDto bankStockDto = new BankStockDto();
        bankStockDto.setListingId(listingId);
        bankStockDto.setListingType(listingType.name());
        HttpEntity<BankStockDto> request = new HttpEntity<>(bankStockDto);

        ResponseEntity<Double> response = restTemplate.postForEntity(url, request, Double.class);
        return response.getBody();
    }
}
