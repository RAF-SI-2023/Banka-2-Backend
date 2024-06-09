package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.ActiveTradingJob;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.exception.OrderNotFoundException;
import rs.edu.raf.BankService.mapper.OrderMapper;
import rs.edu.raf.BankService.repository.*;
import rs.edu.raf.BankService.service.*;
import rs.edu.raf.BankService.service.tradingSimulation.TradingJob;
import rs.edu.raf.BankService.service.tradingSimulation.TradingSimulation;
import rs.edu.raf.BankService.springSecurityUtil.SpringSecurityUtil;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final TransactionService transactionService;
    private final IAMService iamService;
    private final StockService stockService;
    private final CurrencyExchangeService currencyExchangeService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final CashAccountRepository cashAccountRepository;
    private final ActiveTradingJobRepository activeTradingJobRepository;
    private final SecuritiesOwnershipRepository securitiesOwnershipRepository;
    private final OrderTransactionRepository orderTransactionRepository;
    private TradingSimulation tradingSimulation;
    private final BlockingDeque<TradingJob> orders = new LinkedBlockingDeque<>();
    private final ActionAgentProfitService actionAgentProfitService;

    @Autowired
    public OrderServiceImpl(TransactionService transactionService, IAMServiceImpl iamService, StockService stockService, CurrencyExchangeService currencyExchangeService, OrderMapper orderMapper, OrderRepository orderRepository, CashAccountRepository cashAccountRepository, SecuritiesOwnershipRepository securitiesOwnershipRepository, ActiveTradingJobRepository activeTradingJobRepository,OrderTransactionRepository orderTransactionRepository,ActionAgentProfitService actionAgentProfitService) {
        this.transactionService = transactionService;
        this.iamService = iamService;
        this.stockService = stockService;
        this.currencyExchangeService = currencyExchangeService;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.cashAccountRepository = cashAccountRepository;
        this.securitiesOwnershipRepository = securitiesOwnershipRepository;
        this.activeTradingJobRepository = activeTradingJobRepository;
        this.orderTransactionRepository=orderTransactionRepository;
        this.actionAgentProfitService=actionAgentProfitService;


        //load active trading jobs
        List<ActiveTradingJob> atjList = activeTradingJobRepository.findAll();
        atjList.forEach(atj -> {

            try {
                if (atj.isActive())
                    orders.put(new TradingJob(orderRepository.findById(atj.getOrderId()).get()
                            , new ExchangeDto(0L, null, atj.getExchangeDtoAcronym(), null, null, null, 0)
                            , atj.getTradingAccountNumber()
                            , atj.getUserRole(),
                            atj.getTotalPriceCalculated()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        this.tradingSimulation = new TradingSimulation(transactionService, iamService, stockService, currencyExchangeService, orderRepository, cashAccountRepository, securitiesOwnershipRepository, activeTradingJobRepository,orderTransactionRepository,actionAgentProfitService);
        this.tradingSimulation.setTradingJobs(orders);
        Thread thread = new Thread(this.tradingSimulation);
        thread.start();
    }

    @Override
    @Transactional
    public boolean createOrder(OrderDto orderDto) {
        boolean isBankOrder = SpringSecurityUtil.isAgent() || SpringSecurityUtil.isSupervisor();

        Order order = orderMapper.orderDtoToOrder(orderDto);
        order.setOwnedByBank(isBankOrder);

        Long initiatedByUserId = SpringSecurityUtil.getPrincipalId();
        order.setInitiatedByUserId(initiatedByUserId);

        CashAccount tradingCashAccount = fetchPrimaryTradingAccount((isBankOrder ? "bankAccount@bank.rs" : SpringSecurityUtil.getPrincipalEmail()), "Primary trading account not found");

        Object listingDto = fetchSecuritiesByOrder(order);
        double totalPrice = 0;

        ExchangeDto exchangeDto = null;
        String currency = null;
        switch (order.getOrderActionType()) {
            case BUY -> {
                switch (orderDto.getListingType()){
                    case STOCK,FOREX -> {
                        if(((ListingDto) listingDto).getCurrency()==null){
                            exchangeDto = fetchExchangeByExchangeAcronym(((ListingDto) listingDto).getExchange());
                            currency = exchangeDto.getCurrency();
                        }
                        else currency= ((ListingDto) listingDto).getCurrency();
                        totalPrice = calculateOrderPrice(order.getQuantity(), ((ListingDto) listingDto).getPrice());
                    }
                    case OPTION -> {
                        currency = ((OptionDto) listingDto).getCurrency();
                        totalPrice = calculateOrderPrice(order.getQuantity(), ((OptionDto) listingDto).getStrikePrice());
                        order.setSettlementDate(((OptionDto) listingDto).getSettlementDate());
                    }
                    case FUTURE -> {
                        currency= "RSD";
                        totalPrice=(calculateOrderPrice(order.getQuantity(),((FuturesContractDto)listingDto).getFuturesContractPrice()));
                        order.setSettlementDate(((FuturesContractDto) listingDto).getSettlementDate());
                    }
                    }
                }

            case SELL -> {
                switch (orderDto.getListingType()){
                    case STOCK,FOREX -> {
                        if (((ListingDto) listingDto).getExchange() != null) {
                            exchangeDto = fetchExchangeByExchangeAcronym(((ListingDto) listingDto).getExchange());
                            currency = exchangeDto.getCurrency();   //ako bira kom exchangeu ce da proda
                        }
                        else currency = tradingCashAccount.getCurrencyCode(); //ako ne bira, videti u nekom trenutku //todo
                        totalPrice = calculateOrderPrice(order.getQuantity(), ((ListingDto) listingDto).getPrice());
                    }
                    case OPTION -> {

                    }
                }
            }
        }

        if (isBankOrder) {
            handleIfOrderInitiatedByAgent(order, initiatedByUserId, currency, totalPrice);
        }


        double totalPriceInTradingCashAccountCurrency = currencyExchangeService.calculateAmountBetweenCurrencies(currency, tradingCashAccount.getCurrencyCode(), totalPrice);
       try {
           if (order.getOrderStatus() == OrderStatus.APPROVED) {
               transactionService.reserveFunds(tradingCashAccount, totalPriceInTradingCashAccountCurrency);
           }
       }catch (RuntimeException e){
           throw new RuntimeException("Insufficient funds for reservation");
       }

        order = orderRepository.save(order);
        System.out.println("ZAPOCET ORDER "+ order);
        try {
            orders.put(new TradingJob(order, exchangeDto, tradingCashAccount.getAccountNumber(), SpringSecurityUtil.getUserRole(), totalPriceInTradingCashAccountCurrency));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public List<OrderDto> getAll() {
        return orderRepository.findAll().stream().map(orderMapper::orderToOrderDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setOrderStatus(status);
        orderRepository.save(order);
        if (status == OrderStatus.APPROVED &&
                orders.contains(new TradingJob(order, null, null, null, 0))) {
            TradingJob tj = orders.stream().filter(val -> Objects.equals(val.getOrder().getId(), orderId)).toList().get(0);
            System.out.println(tj.getTradingAccountNumber());
            System.out.println(tj.getTotalPriceCalculated());
            transactionService.reserveFunds(tj.getTradingAccountNumber(), tj.getTotalPriceCalculated());
            tj.setOrder(order);
        }
        return true;
    }

    @Override
    public List<Order> findAllByUserId(Long id) {
        return null;
    }

    @Override
    public OrderDto findDtoById(Long orderId) {
        return orderRepository.findById(orderId).map(orderMapper::orderToOrderDto).orElseThrow(OrderNotFoundException::new);

    }

    @Override
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

//    private double getBuyOrderPrice(Order buyOrder, SecuritiesPriceDto currentPrice) {
//        int contractSize = getContractSize(buyOrder.getListingType());
//        //da li je order 'Stop-Limit order'
//        if(buyOrder.getStopPrice() != -1 && buyOrder.getLimitPrice() != -1){
//            return 0.0;
//        }
//        // da li je order 'Stop order'?
//        else if(buyOrder.getStopPrice() != -1){
//            // kupovina stop ordera je moguca samo ako vazi high price > stop price
//            if(currentPrice.getHigh() <= buyOrder.getStopPrice()){
//                return -1.0;
//            }
//            // ako je high price > stop price, order postaje 'Market order'
//            return contractSize * currentPrice.getHigh();
//        }
//        // da li je order 'Limit order'?
//        else if(buyOrder.getLimitPrice() != -1){
//            // pitamo se da li je trenutna cena hartije veca od limit price-a, ako jeste, onda necemo da je kupimo
//            if(currentPrice.getPrice() >= buyOrder.getLimitPrice()){
//                return -1.0;
//            }
//            return contractSize * ((currentPrice.getHigh() < buyOrder.getLimitPrice()) ? currentPrice.getHigh() : buyOrder.getLimitPrice());
//        }
//        // ako nije nista od predhodnog onda je u pitanju 'Market order'
//        else{
//            return contractSize * currentPrice.getHigh();
//        }
//    }
//
//    private double getSellOrderPrice(Order sellOrder, SecuritiesPriceDto currentPrice){
//        int contractSize = getContractSize(sellOrder.getListingType());
//        double sellPrice = 0.0;
//        //da li je order 'Stop-Limit order'
//        if (sellOrder.getStopPrice() != -1 && sellOrder.getLimitPrice() != -1) {
//            return 0.0;
//        }
//        // da li je order 'Stop order'?
//        else if (sellOrder.getStopPrice() != -1) {
//            if (currentPrice.getLow() >= sellOrder.getStopPrice()) {
//                return -1.0;
//            }
//            return contractSize * currentPrice.getLow();
//        }
//        // da li je order 'Limit order'?
//        else if (sellOrder.getLimitPrice() != -1) {
//            if (currentPrice.getPrice() <= sellOrder.getLimitPrice()) {
//                return -1.0;
//            }
//            return contractSize * ((currentPrice.getLow() > sellOrder.getLimitPrice()) ? currentPrice.getLow() : sellOrder.getLimitPrice());
//        }
//        // ako nije nista od predhodnog onda je u pitanju 'Market order'
//        else {
//            return contractSize * currentPrice.getLow();
//        }
//    }

    private int getContractSize(ListingType listingType) {
        return switch (listingType) {
            case STOCK -> 1;
            case FOREX -> 1000;
            case FUTURE -> 0;
            case OPTION -> 100;
        };
    }

    private CashAccount fetchPrimaryTradingAccount(String email, String msg) {
        CashAccount cashAccount = cashAccountRepository.findPrimaryTradingAccount(email);
        if (cashAccount == null) {
            throw new NotFoundException(msg);
        }
        return cashAccount;
    }

    private Object fetchSecuritiesByOrder(Order order) {
        Object listingDto = stockService.getSecuritiesByOrder(order);
        if (listingDto == null) {
            throw new NullPointerException("Null pointer exception when tried to get current price for symbol " + order.getListingSymbol());
        }
        return listingDto;
    }

    private ExchangeDto fetchExchangeByExchangeAcronym(String exchangeAcronym) {
        ExchangeDto exchangeDto = stockService.getExchangeExchangeAcronym(exchangeAcronym);
        if (exchangeDto == null) {
            throw new NullPointerException("Null pointer exception when tried to get exchangeDto by exchangeAcronym: " + exchangeAcronym);
        }
        return exchangeDto;
    }

    private Double calculateOrderPrice(Integer quantity, Double securitiesCurrentPrice) {
        Double total = quantity * securitiesCurrentPrice;
        if (total == null) {
            throw new NullPointerException("Null pointer exception when tried to calculate total price for order");
        }
        return total;
    }

    private void handleIfOrderInitiatedByAgent(Order order, long initiatedByUserId, String exchangeCurrency, double totalPriceInUsd) {
        if (SpringSecurityUtil.isAgent()) {
            // treba da se izracuna ukupna cena ordera i da se konvertuje u default valutu (RSD)
            double limitToSubtractInDefaultCurrency = currencyExchangeService.calculateAmountInDefaultCurrency(exchangeCurrency, totalPriceInUsd);
            if (!iamService.reduceAgentLimit(initiatedByUserId, limitToSubtractInDefaultCurrency)) {
                throw new RuntimeException("Agent's limit not reduced");
            }
            if (iamService.isApprovalNeeded(initiatedByUserId)) {
                order.setOrderStatus(OrderStatus.WAITING_FOR_APPROVAL);
            }
        }
    }

}
