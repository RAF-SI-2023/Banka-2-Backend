package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.BankService.data.dto.BankStockDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.mapper.MarginsTransactionMapper;
import rs.edu.raf.BankService.repository.MarginsAccountRepository;
import rs.edu.raf.BankService.repository.MarginsTransactionRepository;
import rs.edu.raf.BankService.service.MarginsTransactionService;
import rs.edu.raf.BankService.service.OrderService;

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
    public MarginsTransactionDto createTransaction(MarginsTransactionDto marginsTransactionDto) {
        // ostaje da se azuziraju podaci na marznom nalogu
        MarginsAccount account = marginsAccountRepository.findById(marginsTransactionDto.getMarginsAccountId())
                .orElseThrow(() -> new RuntimeException("Margin account not found"));
        Order order = orderService.findById(marginsTransactionDto.getOrderId());
        Double listPricePerUnit = getListingPrice(order.getListingId(), order.getListingType());
        Double orderPrice = order.getQuantity() * listPricePerUnit;

        // moramo da vidimo sta se sve dobija ja fronta, a sta jurimo na backu
        Double initialMargin =  marginsTransactionDto.getInitialMargin();

        MarginsTransaction transaction = transactionMapper.toEntity(marginsTransactionDto);
        transaction.setInvestmentAmount(initialMargin);

        Double loan = orderPrice - initialMargin;
        if (loan < 0) {
            loan = 0.0;
        }

        Double interest = loan * 0.05;
        if (order.getListingType().equals(ListingType.FUTURE)) {
            interest = 0.0;
        }

        transaction.setLoanValue(loan);
        transaction.setInterest(interest);
        transaction.setInvestmentAmount(initialMargin);
        marginsTransactionRepository.save(transaction);

        return marginsTransactionDto;
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
