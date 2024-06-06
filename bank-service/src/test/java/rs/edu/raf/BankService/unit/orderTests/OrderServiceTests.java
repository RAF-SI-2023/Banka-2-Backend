package rs.edu.raf.BankService.unit.orderTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.OrderActionType;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.exception.OrderNotFoundException;
import rs.edu.raf.BankService.mapper.OrderMapper;
import rs.edu.raf.BankService.repository.*;
import rs.edu.raf.BankService.service.ActionAgentProfitService;
import rs.edu.raf.BankService.service.CurrencyExchangeService;
import rs.edu.raf.BankService.service.StockService;
import rs.edu.raf.BankService.service.TransactionService;
import rs.edu.raf.BankService.service.impl.IAMServiceImpl;
import rs.edu.raf.BankService.service.impl.OrderServiceImpl;
import rs.edu.raf.BankService.springSecurityUtil.SpringSecurityUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    private OrderServiceImpl orderService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private TransactionService transactionService;
    private IAMServiceImpl iamServiceImpl;
    private StockService stockService;
    private CurrencyExchangeService currencyExchangeService;
    private CashAccountRepository cashAccountRepository;
    private ActiveTradingJobRepository activeTradingJobRepository;
    private SecuritiesOwnershipRepository securitiesOwnershipRepository;
    private OrderTransactionRepository orderTransactionRepository;
    private ActionAgentProfitService actionAgentProfitService;
    private MockedStatic<SpringSecurityUtil> mockedStatic;

    @BeforeEach
    public void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderMapper = mock(OrderMapper.class);
        transactionService = mock(TransactionService.class);
        iamServiceImpl = mock(IAMServiceImpl.class);
        stockService = mock(StockService.class);
        currencyExchangeService = mock(CurrencyExchangeService.class);
        cashAccountRepository = mock(CashAccountRepository.class);
        activeTradingJobRepository = mock(ActiveTradingJobRepository.class);
        securitiesOwnershipRepository = mock(SecuritiesOwnershipRepository.class);
        orderTransactionRepository = mock(OrderTransactionRepository.class);
        actionAgentProfitService = mock(ActionAgentProfitService.class);

        orderService = new OrderServiceImpl(transactionService, iamServiceImpl, stockService, currencyExchangeService, orderMapper, orderRepository, cashAccountRepository, securitiesOwnershipRepository, activeTradingJobRepository, orderTransactionRepository, actionAgentProfitService);
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(SpringSecurityUtil.class);
    }

    @AfterEach
    public void teardown() {
        mockedStatic.close();
    }

    @Test
    public void testCreateOrder_Buy_Stock_Forex_Success() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.STOCK);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setOwnedByBank(true);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.BUY);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("USD");
        cashAccount.setEmail("bankAccount@bank.rs");

        ListingDto listingDto = new ListingDto();
        listingDto.setExchange("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        listingDto.setPrice(100.0);
        listingDto.setCurrency(null);

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("USD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = listingDto.getPrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(true);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("bankAccount@bank.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(cashAccountRepository.findPrimaryTradingAccount("bankAccount@bank.rs")).thenReturn(cashAccount);
        when(stockService.getSecuritiesByOrder(order)).thenReturn(listingDto);
        when(stockService.getExchangeExchangeAcronym(listingDto.getExchange())).thenReturn(exchangeDto);
        when(currencyExchangeService.calculateAmountInDefaultCurrency(currency, totalPrice)).thenReturn(100.0);
        when(currencyExchangeService.calculateAmountBetweenCurrencies(currency, cashAccount.getCurrencyCode(), totalPrice)).thenReturn(100.0);
        when(orderRepository.save(order)).thenReturn(order);
        when(iamServiceImpl.reduceAgentLimit(anyLong(), anyDouble())).thenReturn(true);

        boolean result = orderService.createOrder(orderDto);

        assertTrue(result);
        verify(orderRepository, times(1)).save(order);
        verify(transactionService, times(1)).reserveFunds(cashAccount, 100.0);
    }

    @Test
    public void testCreateOrder_Buy_Option_Success() {
        // Arrange
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.OPTION);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setOwnedByBank(true);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.BUY);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("USD");
        cashAccount.setEmail("bankAccount@bank.rs");

        OptionDto optionDto = new OptionDto();
        optionDto.setStrikePrice(100.0);
        optionDto.setCurrency("USD");

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("USD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = optionDto.getStrikePrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(true);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("bankAccount@bank.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(cashAccountRepository.findPrimaryTradingAccount("bankAccount@bank.rs")).thenReturn(cashAccount);
        when(stockService.getSecuritiesByOrder(order)).thenReturn(optionDto);
        when(currencyExchangeService.calculateAmountInDefaultCurrency(currency, totalPrice)).thenReturn(100.0);
        when(currencyExchangeService.calculateAmountBetweenCurrencies(currency, cashAccount.getCurrencyCode(), totalPrice)).thenReturn(100.0);
        when(orderRepository.save(order)).thenReturn(order);
        when(iamServiceImpl.reduceAgentLimit(anyLong(), anyDouble())).thenReturn(true);

        boolean result = orderService.createOrder(orderDto);

        assertTrue(result);
        verify(orderRepository, times(1)).save(order);
        verify(transactionService, times(1)).reserveFunds(cashAccount, 100.0);
    }

    @Test
    public void testCreateOrder_Buy_Future_Success() {
        // Arrange
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.FUTURE);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setOwnedByBank(true);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.BUY);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("RSD");
        cashAccount.setEmail("bankAccount@bank.rs");

        FuturesContractDto futuresContractDto = new FuturesContractDto();
        futuresContractDto.setFuturesContractPrice(100.0);

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("RSD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = futuresContractDto.getFuturesContractPrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(true);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("bankAccount@bank.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(cashAccountRepository.findPrimaryTradingAccount("bankAccount@bank.rs")).thenReturn(cashAccount);
        when(stockService.getSecuritiesByOrder(order)).thenReturn(futuresContractDto);
        when(currencyExchangeService.calculateAmountInDefaultCurrency(currency, totalPrice)).thenReturn(100.0);
        when(currencyExchangeService.calculateAmountBetweenCurrencies(currency, cashAccount.getCurrencyCode(), totalPrice)).thenReturn(100.0);
        when(orderRepository.save(order)).thenReturn(order);
        when(iamServiceImpl.reduceAgentLimit(anyLong(), anyDouble())).thenReturn(true);

        boolean result = orderService.createOrder(orderDto);

        assertTrue(result);
        verify(orderRepository, times(1)).save(order);
        verify(transactionService, times(1)).reserveFunds(cashAccount, 100.0);
    }

    @Test
    public void testCreateOrder_Sell_Stock_Forex_Success() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.STOCK);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setOwnedByBank(true);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.SELL);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("USD");
        cashAccount.setEmail("bankAccount@bank.rs");

        ListingDto listingDto = new ListingDto();
        listingDto.setExchange("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        listingDto.setPrice(100.0);
        listingDto.setCurrency(null);

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("USD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = listingDto.getPrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(true);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("bankAccount@bank.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(cashAccountRepository.findPrimaryTradingAccount("bankAccount@bank.rs")).thenReturn(cashAccount);
        when(stockService.getSecuritiesByOrder(order)).thenReturn(listingDto);
        when(stockService.getExchangeExchangeAcronym(listingDto.getExchange())).thenReturn(exchangeDto);
        when(currencyExchangeService.calculateAmountInDefaultCurrency(currency, totalPrice)).thenReturn(100.0);
        when(currencyExchangeService.calculateAmountBetweenCurrencies(currency, cashAccount.getCurrencyCode(), totalPrice)).thenReturn(100.0);
        when(orderRepository.save(order)).thenReturn(order);
        when(iamServiceImpl.reduceAgentLimit(anyLong(), anyDouble())).thenReturn(true);

        boolean result = orderService.createOrder(orderDto);

        assertTrue(result);
        verify(orderRepository, times(1)).save(order);
        verify(transactionService, times(1)).reserveFunds(cashAccount, 100.0);
    }

    @Test
    public void testCreateOrder_Not_Approved_Success() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.STOCK);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setOwnedByBank(true);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.SELL);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("USD");
        cashAccount.setEmail("bankAccount@bank.rs");

        ListingDto listingDto = new ListingDto();
        listingDto.setExchange("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        listingDto.setPrice(100.0);
        listingDto.setCurrency(null);

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("USD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = listingDto.getPrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(true);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("bankAccount@bank.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(cashAccountRepository.findPrimaryTradingAccount("bankAccount@bank.rs")).thenReturn(cashAccount);
        when(stockService.getSecuritiesByOrder(order)).thenReturn(listingDto);
        when(stockService.getExchangeExchangeAcronym(listingDto.getExchange())).thenReturn(exchangeDto);
        when(currencyExchangeService.calculateAmountInDefaultCurrency(currency, totalPrice)).thenReturn(100.0);
        when(currencyExchangeService.calculateAmountBetweenCurrencies(currency, cashAccount.getCurrencyCode(), totalPrice)).thenReturn(100.0);
        when(orderRepository.save(order)).thenReturn(order);
        when(iamServiceImpl.reduceAgentLimit(anyLong(), anyDouble())).thenReturn(true);
        when(iamServiceImpl.isApprovalNeeded(anyLong())).thenReturn(true);

        boolean result = orderService.createOrder(orderDto);

        assertTrue(result);
        verify(orderRepository, times(1)).save(order);
        verify(transactionService, times(0)).reserveFunds(cashAccount, 100.0);
    }

    @Test
    public void testCreateOrder_Not_Bank_Order_Success() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.STOCK);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        //not owned
        order.setOwnedByBank(false);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.SELL);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("USD");
        cashAccount.setEmail("email@raf.rs");

        ListingDto listingDto = new ListingDto();
        listingDto.setExchange("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        listingDto.setPrice(100.0);
        listingDto.setCurrency(null);

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("USD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = listingDto.getPrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(false);
        when(SpringSecurityUtil.isSupervisor()).thenReturn(false);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("email@raf.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(cashAccountRepository.findPrimaryTradingAccount("email@raf.rs")).thenReturn(cashAccount);
        when(stockService.getSecuritiesByOrder(order)).thenReturn(listingDto);
        when(stockService.getExchangeExchangeAcronym(listingDto.getExchange())).thenReturn(exchangeDto);
        when(currencyExchangeService.calculateAmountBetweenCurrencies(currency, cashAccount.getCurrencyCode(), totalPrice)).thenReturn(100.0);
        when(orderRepository.save(order)).thenReturn(order);

        boolean result = orderService.createOrder(orderDto);

        assertTrue(result);
        verify(orderRepository, times(1)).save(order);
        verify(transactionService, times(1)).reserveFunds(cashAccount, 100.0);
        verify(iamServiceImpl, times(0)).reduceAgentLimit(anyLong(), any());
        verify(iamServiceImpl, times(0)).isApprovalNeeded(anyLong());

    }

    /*
        @Test
        public void testCreateOrder_CashAccount_Not_Found_Failure() {
            // Arrange
            OrderDto orderDto = new OrderDto();
            orderDto.setId(1L);
            orderDto.setListingType(ListingType.STOCK);

            Order order = new Order();
            order.setId(1L);
            order.setQuantity(5);
            order.setOrderStatus(OrderStatus.APPROVED);
            //not owned
            order.setOwnedByBank(false);
            order.setInitiatedByUserId(33L);
            order.setOrderActionType(OrderActionType.SELL);

            CashAccount cashAccount = new CashAccount();
            cashAccount.setCurrencyCode("USD");
            cashAccount.setEmail("email@raf.rs");

            ListingDto listingDto = new ListingDto();
            listingDto.setExchange("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
            listingDto.setPrice(100.0);
            listingDto.setCurrency(null);

            ExchangeDto exchangeDto = new ExchangeDto();
            exchangeDto.setCurrency("USD");
            String currency = exchangeDto.getCurrency();

            double totalPrice = listingDto.getPrice() * order.getQuantity();

            // Mock static method calls
            //mockStatic(SpringSecurityUtil.class);
            when(SpringSecurityUtil.isAgent()).thenReturn(false);
            when(SpringSecurityUtil.isSupervisor()).thenReturn(false);
            when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
            when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("email@raf.rs");
            when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

            when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
            when(cashAccountRepository.findPrimaryTradingAccount("bank@raf.rs")).thenReturn(cashAccount);

            // Assert
            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                orderService.createOrder(orderDto);
            });

            assertEquals("Primary trading account not found", exception.getMessage());
            verify(orderRepository, times(0)).save(order);
            verify(transactionService, times(0)).reserveFunds(cashAccount, 100.0);
            verify(iamServiceImpl, times(0)).reduceAgentLimit(anyLong(), any());
            verify(iamServiceImpl, times(0)).isApprovalNeeded(anyLong());

        }


     */
    @Test
    public void testCreateOrder_Security_By_Order_Null_Failure() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.STOCK);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        //not owned
        order.setOwnedByBank(false);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.SELL);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("USD");
        cashAccount.setEmail("email@raf.rs");

        ListingDto listingDto = new ListingDto();
        listingDto.setExchange("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        listingDto.setPrice(100.0);
        listingDto.setCurrency(null);

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("USD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = listingDto.getPrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(false);
        when(SpringSecurityUtil.isSupervisor()).thenReturn(false);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("email@raf.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            orderService.createOrder(orderDto);
        });

        //assertEquals("Primary trading account not found", exception.getMessage());
        verify(orderRepository, times(0)).save(order);
        verify(transactionService, times(0)).reserveFunds(cashAccount, 100.0);
        verify(iamServiceImpl, times(0)).reduceAgentLimit(anyLong(), any());
        verify(iamServiceImpl, times(0)).isApprovalNeeded(anyLong());

    }

    @Test
    public void testCreateOrder_Acronym_Null_Failure() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.STOCK);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setOwnedByBank(true);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.SELL);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("USD");
        cashAccount.setEmail("bankAccount@bank.rs");

        ListingDto listingDto = new ListingDto();
        listingDto.setExchange("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        listingDto.setPrice(100.0);
        listingDto.setCurrency(null);

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("USD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = listingDto.getPrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(true);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("bankAccount@bank.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(cashAccountRepository.findPrimaryTradingAccount("bankAccount@bank.rs")).thenReturn(cashAccount);
        when(stockService.getSecuritiesByOrder(order)).thenReturn(listingDto);
        when(stockService.getExchangeExchangeAcronym(listingDto.getExchange())).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            orderService.createOrder(orderDto);
        });

        assertEquals("Null pointer exception when tried to get exchangeDto by exchangeAcronym: " + listingDto.getExchange(), exception.getMessage());
        verify(orderRepository, times(0)).save(order);
        verify(transactionService, times(0)).reserveFunds(cashAccount, 100.0);
        verify(iamServiceImpl, times(0)).reduceAgentLimit(anyLong(), any());
        verify(iamServiceImpl, times(0)).isApprovalNeeded(anyLong());

    }

    @Test
    public void testCreateOrder_ReduceAgent_Failure() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setListingType(ListingType.FUTURE);

        Order order = new Order();
        order.setId(1L);
        order.setQuantity(5);
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setOwnedByBank(true);
        order.setInitiatedByUserId(33L);
        order.setOrderActionType(OrderActionType.BUY);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("RSD");
        cashAccount.setEmail("bankAccount@bank.rs");

        FuturesContractDto futuresContractDto = new FuturesContractDto();
        // optionDto.set("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        futuresContractDto.setFuturesContractPrice(100.0);
        //futuresContractDto.setCurrency("RSD");

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setCurrency("RSD");
        String currency = exchangeDto.getCurrency();

        double totalPrice = futuresContractDto.getFuturesContractPrice() * order.getQuantity();

        when(SpringSecurityUtil.isAgent()).thenReturn(true);
        when(SpringSecurityUtil.getPrincipalId()).thenReturn(33L);
        when(SpringSecurityUtil.getPrincipalEmail()).thenReturn("bankAccount@bank.rs");
        when(SpringSecurityUtil.getUserRole()).thenReturn("AGENT");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(cashAccountRepository.findPrimaryTradingAccount("bankAccount@bank.rs")).thenReturn(cashAccount);
        when(stockService.getSecuritiesByOrder(order)).thenReturn(futuresContractDto);
        when(currencyExchangeService.calculateAmountInDefaultCurrency(currency, totalPrice)).thenReturn(100.0);
        when(iamServiceImpl.reduceAgentLimit(anyLong(), anyDouble())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderDto);
        });

        assertEquals("Agent's limit not reduced", exception.getMessage());
        verify(orderRepository, never()).save(order);
        verify(currencyExchangeService, never()).calculateAmountBetweenCurrencies(anyString(), anyString(), anyDouble());
        verify(transactionService, never()).reserveFunds(cashAccount, 100.0);
        verify(iamServiceImpl, never()).isApprovalNeeded(anyLong());

    }


    @Test
    public void testGetAll() {
        OrderDto orderDto1 = new OrderDto();
        OrderDto orderDto2 = new OrderDto();
        List<OrderDto> expectedOrders = Arrays.asList(orderDto1, orderDto2);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(new Order(), new Order()));
        when(orderMapper.orderToOrderDto(new Order())).thenReturn(orderDto1);
        when(orderMapper.orderToOrderDto(new Order())).thenReturn(orderDto2);

        List<OrderDto> actualOrders = orderService.getAll();

        assertEquals(expectedOrders.size(), actualOrders.size());
    }

    @Test
    public void testUpdateOrderStatus_Success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        OrderStatus status = OrderStatus.APPROVED;

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        boolean updated = orderService.updateOrderStatus(orderId, status);

        assertEquals(true, updated);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testUpdateOrderStatus_OrderNotFound() {
        Long orderId = 1L;
        OrderStatus status = OrderStatus.APPROVED;

        when(orderRepository.findById(orderId)).thenThrow(OrderNotFoundException.class);

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatus(orderId, status));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any());
    }


    @Test
    public void testFindDtoById_Success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        OrderDto expectedOrderDto = new OrderDto();
        expectedOrderDto.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDto(order)).thenReturn(expectedOrderDto);

        OrderDto actualOrderDto = orderService.findDtoById(orderId);

        assertNotNull(actualOrderDto);
        assertEquals(expectedOrderDto.getId(), actualOrderDto.getId());
    }

    @Test
    public void testFindDtoById_OrderNotFound() {
        Long orderId = 1L;

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderService.findDtoById(orderId));

        assertEquals("Order not found", exception.getMessage());
        verify(orderMapper, never()).orderToOrderDto(any());
    }

    @Test
    public void testFindById_Success() {
        Long orderId = 1L;
        Order expectedOrder = new Order();
        expectedOrder.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        Order actualOrder = orderService.findById(orderId);

        assertNotNull(actualOrder);
        assertEquals(expectedOrder.getId(), actualOrder.getId());
    }

    @Test
    public void testFindById_OrderNotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderService.findById(orderId));
        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository).findById(orderId);
    }


}
