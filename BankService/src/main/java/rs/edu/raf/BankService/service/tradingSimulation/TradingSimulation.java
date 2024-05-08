package rs.edu.raf.BankService.service.tradingSimulation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.OrderActionType;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.data.enums.WorkingHoursStatus;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.service.CurrencyExchangeService;
import rs.edu.raf.BankService.service.IAMService;
import rs.edu.raf.BankService.service.StockService;
import rs.edu.raf.BankService.service.TransactionService;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@RequiredArgsConstructor
@Getter
@Setter
public class TradingSimulation implements Runnable {

    private TransactionService transactionService;
    private IAMService iamService;
    private StockService stockService;
    private CurrencyExchangeService currencyExchangeService;
    private OrderRepository orderRepository;
    private CashAccountRepository cashAccountRepository;
    private Random random = new Random();
    private static BlockingQueue<TradingJob> tradingJobs = new LinkedBlockingQueue<>();
    private static final Object lock = new Object();

    @Override
    public void run() {
        try {
            processOrders();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void processOrders() throws InterruptedException {
        while (true) {

            Thread.sleep(100);

            TradingJob tradingJob = tradingJobs.take();

            synchronized (TradingSimulation.lock) {
                if (tradingJob.getOrder().getOrderStatus() != OrderStatus.APPROVED) {
                    tradingJobs.put(tradingJob);
                    continue;
                }
                switch (tradingJob.getOrder().getOrderActionType()) {
                    case BUY -> processBuyOrder(tradingJob);
                    case SELL -> processSellOrder(tradingJob);
                }
            }

        }
    }

    private void processBuyOrder(TradingJob tradingJob) {
        if (tradingJob.getOrder().getOrderActionType() == OrderActionType.BUY) {
            switch (tradingJob.getOrder().getListingType()) {
                case STOCK -> processStockBuyOrder(tradingJob);
                case FOREX -> processForexBuyOrder(tradingJob);
            }
        }
    }

    // TODO
    private void processStockBuyOrder(TradingJob buyTradingJob) {
        TradingJob sellTradingJob = null;
        for (TradingJob job : tradingJobs) {
            if (job.getOrder().getOrderActionType() == OrderActionType.SELL) {
                if (job.getOrder().getListingSymbol().equals(buyTradingJob.getOrder().getListingSymbol())) {
                    sellTradingJob = job;
                    break;
                }
            }
        }
        if (sellTradingJob != null) {
            Order sellOrder = sellTradingJob.getOrder();
            Order buyOrder = buyTradingJob.getOrder();
            ListingDto listingDto = fetchSecuritiesByOrder(sellOrder);

            boolean doNotProcessOrder =
                    (sellOrder.isAllOrNone() && buyOrder.isAllOrNone() && sellOrder.getQuantity() != buyOrder.getQuantity()) ||
                    (sellOrder.isAllOrNone() && (sellOrder.getQuantity() > buyOrder.getQuantity())) ||
                    (buyOrder.isAllOrNone() && (buyOrder.getQuantity() > sellOrder.getQuantity())) ||
                    !checkLimitPrice(sellOrder, listingDto.getLow(), listingDto.getHigh()) ||
                    !checkLimitPrice(buyOrder, listingDto.getLow(), listingDto.getHigh()) ||
                    !checkStopPrice(sellOrder, listingDto.getLow(), listingDto.getHigh()) ||
                    !checkStopPrice(buyOrder, listingDto.getLow(), listingDto.getHigh());

            if (doNotProcessOrder) {
                //
                try {
                    tradingJobs.put(buyTradingJob);
                    tradingJobs.put(sellTradingJob);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }


            Integer quantityToProcess = Math.min(sellOrder.getQuantity(), buyOrder.getQuantity());

            // POSTAVLJA SE PITANJE U KOJOJ VALUTI TREBA DA SE RACUNA CENA
            // MISLIM DA JE OKEJ DA SE KORISTI VALUTA RACUNA KORISNIKA KOJI PRODAJE AKCIJE
            double totalPrice = quantityToProcess * listingDto.getPrice();
            transactionService.transferFunds(buyTradingJob.getTradingAccountNumber(), sellTradingJob.getTradingAccountNumber(), totalPrice);

            // ...

        } else {
            // KUPUJEMO DIREKTNO SA BERZE
            // SVAKA BERZA IMA VALUTU U KOJOJ POSLUJE
            Order order = buyTradingJob.getOrder();
            WorkingHoursStatus workingHoursStatus = getWorkingHoursForStock(order.getListingId());
            if (workingHoursStatus == WorkingHoursStatus.CLOSED) {
                try {
                    tradingJobs.put(buyTradingJob);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            ListingDto listingDto = fetchSecuritiesByOrder(order);
            ExchangeDto exchangeDto = fetchExchangeByExchangeAcronym(listingDto.getExchange());

            // ...
        }

    }

    // TODO
    private void processForexBuyOrder(TradingJob tradingJob) {
        Order order = tradingJob.getOrder();

    }

    // TODO
    // KADA SE RADI SELL ORDER-a,
    private void processSellOrder(TradingJob tradingJob) {
        if (tradingJob.getOrder().getOrderActionType() == OrderActionType.SELL) {
            switch (tradingJob.getOrder().getListingType()) {
                case STOCK -> processStockSellOrder(tradingJob);
                case FOREX -> processForexSellOrder(tradingJob);
            }
        }
    }

    // TODO
    private void processStockSellOrder(TradingJob tradingJob) {
        Order order = tradingJob.getOrder();
        WorkingHoursStatus workingHoursStatus = getWorkingHoursForStock(order.getListingId());
        if (workingHoursStatus == WorkingHoursStatus.CLOSED) {
            return;
        }

    }

    // TODO
    private void processForexSellOrder(TradingJob tradingJob) {

    }

    // TODO
    private WorkingHoursStatus getWorkingHoursForStock(Long listingId) {
        return WorkingHoursStatus.OPENED;
    }

    private ListingDto fetchSecuritiesByOrder(Order order) {
        ListingDto listingDto = stockService.getSecuritiesByOrder(order);
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


    private Double calculateTotalPrice(ListingDto listingDto, Order order) {
        if (listingDto instanceof StockDto) {
            return calculateStockTotalPrice((StockDto) listingDto, order);
        } else if (listingDto instanceof ForexDto) {
            return calculateForexTotalPrice((ForexDto) listingDto, order);
        }
        return null;
    }

    private Double calculateStockTotalPrice(StockDto stockDto, Order order) {

        int quantity = Math.min(stockDto.getShares(), order.getQuantity());
        // cenu hartije treba da simuliramo jer su cene staticne na stock servisu
        double orderSimulatedPrice = stockDto.getPrice() + random.nextDouble(-500, 500);


        return null;
    }

    // mock-ujemo kolicinu koja se kupuje,
    // jer su podaci koji se dovlace sa stock servisa staticni
    private Integer mockQuantity(Integer realQuantity) {
        return random.nextInt(realQuantity);
    }

    // mock-ujemo kolicinu koja se kupuje,
    // jer su podaci koji se dovlace sa stock servisa staticni
    private Double mockPrice(Double realPrice) {
        return realPrice + random.nextDouble(-(realPrice / 4), (realPrice / 4));
    }

    private Double calculateForexTotalPrice(ForexDto forexDto, Order order) {
        return null;
    }

    public void setTradingJobs(BlockingDeque<TradingJob> tradingJobs) {
        TradingSimulation.tradingJobs = tradingJobs;
    }

    private int getContractSize(ListingType listingType) {
        return switch (listingType) {
            case STOCK -> 1;
            case FOREX -> 1000;
            case FUTURE -> 0;
            case OPTION -> 100;
        };
    }

    private boolean checkLimitPrice(Order order, double low, double high) {
        return order.getLimitPrice() == -1 ||
                ((order.getOrderActionType() == OrderActionType.SELL) ? low > order.getLimitPrice() : high < order.getLimitPrice());

    }

    private boolean checkStopPrice(Order order, double low, double high) {
        return order.getStopPrice() == -1 ||
                ((order.getOrderActionType() == OrderActionType.SELL) ? low < order.getStopPrice() : high > order.getStopPrice());
    }

}

