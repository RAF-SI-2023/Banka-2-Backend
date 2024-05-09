package rs.edu.raf.BankService.service.tradingSimulation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.OrderActionType;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.data.enums.WorkingHoursStatus;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.CurrencyExchangeService;
import rs.edu.raf.BankService.service.IAMService;
import rs.edu.raf.BankService.service.StockService;
import rs.edu.raf.BankService.service.TransactionService;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static rs.edu.raf.BankService.data.enums.ListingType.FOREX;


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
    private SecuritiesOwnershipRepository securitiesOwnershipRepository;
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
                    case SELL_TO_STOCK_MARKET -> processSellToStockMarketOrder(tradingJob);
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
        //TODO za sada je ovako jer forex samo poziva ovu metodu, ako budemo odvajali metode, onda izbrisati ovo.
        if (buyTradingJob.getOrder().getListingType() == FOREX) {
            sellTradingJob = null;
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
            //mora i ovo da se updateuje zar ne?
            //trebalo bi da bude unique email + securityName as a key
            List<SecuritiesOwnership> buySecurities = securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(buyTradingJob.getTradingAccountNumber(), listingDto.getSymbol());
            List<SecuritiesOwnership> sellSecurities = securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(sellTradingJob.getTradingAccountNumber(), listingDto.getSymbol());

            if (buySecurities.isEmpty()) {
                //kreiraj
                SecuritiesOwnership so = new SecuritiesOwnership();
                so.setAccountNumber(buyTradingJob.getTradingAccountNumber());
                so.setEmail(cashAccountRepository.findByAccountNumber(buyTradingJob.getTradingAccountNumber()).getEmail());
                so.setQuantity(0);
                so.setSecuritiesSymbol(listingDto.getSymbol());
                so.setOwnedByBank(false);
                so.setQuantityOfPubliclyAvailable(0);
                securitiesOwnershipRepository.save(so);
                buySecurities.add(so);
            }
            if (sellSecurities.isEmpty()) {
                try {
                    tradingJobs.put(buyTradingJob);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }


            //uzece najmanje koji buy moze da kupi ili sell moze da proda
            Integer quantityToProcess = Math.min(sellOrder.getQuantity() - sellOrder.getRealizedQuantity(), buyOrder.getQuantity() - buyOrder.getRealizedQuantity());

            // POSTAVLJA SE PITANJE U KOJOJ VALUTI TREBA DA SE RACUNA CENA
            // MISLIM DA JE OKEJ DA SE KORISTI VALUTA RACUNA KORISNIKA KOJI PRODAJE AKCIJE
            double totalPrice = quantityToProcess * listingDto.getPrice();
            transactionService.transferFunds(buyTradingJob.getTradingAccountNumber(), sellTradingJob.getTradingAccountNumber(), totalPrice);

            // ...
            SecuritiesOwnership buyerSo = buySecurities.get(0);
            SecuritiesOwnership sellerSo = sellSecurities.get(0);
            buyerSo.setQuantity(buyerSo.getQuantity() + quantityToProcess);
            sellerSo.setQuantity(sellerSo.getQuantity() - quantityToProcess);
            securitiesOwnershipRepository.saveAll(List.of(buyerSo, sellerSo));
            //update-ujem ordere tako da se gleda i realizovani quantity za slucaj da se samo deo ordera zavrsi
            buyOrder.setRealizedQuantity(buyOrder.getRealizedQuantity() + quantityToProcess);
            sellOrder.setRealizedQuantity(sellOrder.getRealizedQuantity() + quantityToProcess);
            if (Objects.equals(buyOrder.getRealizedQuantity(), buyOrder.getQuantity())) {
                buyOrder.setDone(true);
            }
            if (Objects.equals(sellOrder.getRealizedQuantity(), sellOrder.getQuantity())) {
                sellOrder.setDone(true);
            }
            orderRepository.saveAll(List.of(buyOrder, sellOrder));
            //ako neki od njih i dalje nije gotov ( nije realizovan ceo quantity, vraca se da se radi)
            try {
                if (!buyOrder.isDone())
                    tradingJobs.put(buyTradingJob);
                if (!sellOrder.isDone())
                    tradingJobs.put(sellTradingJob);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //TODO videti da li treba jos nesto???

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
            //mockujemo podatke
            listingDto.setPrice(mockPrice(listingDto.getPrice()));
            listingDto.setVolume(mockQuantity(listingDto.getVolume()));

            CashAccount account = cashAccountRepository.findByAccountNumber(buyTradingJob.getTradingAccountNumber());

            boolean doNotProcessOrder =
                    (order.isAllOrNone() && !Objects.equals(listingDto.getVolume(), order.getQuantity())) ||
                            (order.isAllOrNone() && (order.getQuantity() > listingDto.getVolume())) ||
                            !checkLimitPrice(order, listingDto.getLow(), listingDto.getHigh()) ||
                            !checkStopPrice(order, listingDto.getLow(), listingDto.getHigh());

            if (doNotProcessOrder) {
                try {
                    tradingJobs.put(buyTradingJob);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            //mora i ovo da se updateuje zar ne?
            //trebalo bi da bude unique acc# + securityName as a key
            List<SecuritiesOwnership> buySecurities = securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(buyTradingJob.getTradingAccountNumber(), listingDto.getSymbol());
            if (buySecurities.isEmpty()) {
                //kreiraj
                SecuritiesOwnership so = new SecuritiesOwnership();
                so.setAccountNumber(buyTradingJob.getTradingAccountNumber());
                so.setEmail(cashAccountRepository.findByAccountNumber(buyTradingJob.getTradingAccountNumber()).getEmail());
                so.setQuantity(0);
                so.setSecuritiesSymbol(listingDto.getSymbol());
                so.setOwnedByBank(false);
                so.setQuantityOfPubliclyAvailable(0);
                securitiesOwnershipRepository.save(so);
                buySecurities.add(so);
            }
            //uzece najmanje koji buy moze da kupi ili sell moze da proda
            Integer quantityToProcess = Math.min(listingDto.getVolume(), order.getQuantity() - order.getRealizedQuantity());

            // POSTAVLJA SE PITANJE U KOJOJ VALUTI TREBA DA SE RACUNA CENA
            // MISLIM DA JE OKEJ DA SE KORISTI VALUTA RACUNA KORISNIKA KOJI PRODAJE AKCIJE
            double totalPrice = quantityToProcess * listingDto.getPrice();

            //menjanje valute //TODo Mozda ovde treba da se zamene parametri, proveriti u nekom trenutku
            totalPrice = currencyExchangeService.calculateAmountBetweenCurrencies(exchangeDto.getCurrency(), account.getCurrencyCode(), totalPrice);
            //KOME DATI KES? trenutno samo releasuje funds u abyss, tako po specifikaciji
            transactionService.releaseFunds(account, totalPrice);
            // ...
            SecuritiesOwnership buyerSo = buySecurities.get(0);
            buyerSo.setQuantity(buyerSo.getQuantity() + quantityToProcess);
            securitiesOwnershipRepository.save(buyerSo);
            //update-ujem ordere tako da se gleda i realizovani quantity za slucaj da se samo deo ordera zavrsi
            order.setRealizedQuantity(order.getRealizedQuantity() + quantityToProcess);
            if (Objects.equals(order.getRealizedQuantity(), order.getQuantity())) {
                order.setDone(true);
            }
            orderRepository.save(order);

            try {
                if (!order.isDone())
                    tradingJobs.put(buyTradingJob);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //TODO videti da li treba jos nesto???

            //  listingDto.
            // ...
        }

    }

    // TODO pp da je logika za stock=forex, a zabranjeno u endpointu
    private void processForexBuyOrder(TradingJob tradingJob) {
        processStockBuyOrder(tradingJob);
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

    private void processSellToStockMarketOrder(TradingJob tradingJob) {
        if (tradingJob.getOrder().getOrderActionType() != OrderActionType.SELL_TO_STOCK_MARKET) {
            throw new RuntimeException("wrong order type");
        }
        Order order = tradingJob.getOrder();
        ListingDto listingDto = fetchSecuritiesByOrder(order);
        if (!order.isDone() && order.getQuantity() - order.getRealizedQuantity() > 0) {
            double amountToReceive = (order.getQuantity() - order.getRealizedQuantity()) * listingDto.getPrice();
            //todo videti da li treba da se konvertuje
            transactionService.releaseFunds(tradingJob.getTradingAccountNumber(), amountToReceive);
            List<SecuritiesOwnership> so = securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(tradingJob.getTradingAccountNumber(), listingDto.getSymbol());
            if (so.size() == 1) {
                so.get(0).setQuantity(order.getQuantity() - order.getRealizedQuantity());
            } else {
                //ovde je greska?
                so.get(0).setQuantity(order.getQuantity() - order.getRealizedQuantity());
                throw new RuntimeException("nesto nije ok bas");
            }
            securitiesOwnershipRepository.saveAll(so);
            order.setRealizedQuantity(order.getQuantity() - order.getRealizedQuantity());
            order.setDone(true);
            orderRepository.save(order);
        }
        order.setDone(true);
        orderRepository.save(order);
    }


    // TODO
    private void processStockSellOrder(TradingJob tradingJob) {
        Order order = tradingJob.getOrder();
        WorkingHoursStatus workingHoursStatus = getWorkingHoursForStock(order.getListingId());
        if (workingHoursStatus == WorkingHoursStatus.CLOSED) {
            return;
        }

        //cela logika se zapravo radi kada neko kupi, tj tad se dobijaju pare i to.
        try {
            tradingJobs.put(tradingJob);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    // TODO  kontam da ce biti isto kao kupovina bar za sad
    private void processForexSellOrder(TradingJob tradingJob) {
        processStockSellOrder(tradingJob);
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

