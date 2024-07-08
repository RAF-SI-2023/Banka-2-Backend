package rs.edu.raf.BankService.service.impl.toIgnore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import org.yaml.snakeyaml.util.UriEncoder;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.service.StockService;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class StockServiceImpl implements StockService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${stock.service.url:http://stock-service:8001/api}")
    private String STOCK_SERVICE_URL;

    @Override
    public ForexDto getForexBySymbol(String symbol) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STOCK_SERVICE_URL + "/forex/by-symbol?symbol=" + symbol))
                //             .header("Authorization", SpringSecurityUtil.getJwtToken())
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        ForexDto forexDto;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonUserListing = response.body();
            forexDto = objectMapper.readValue(jsonUserListing, ForexDto.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException("Forex not found");
        }

        if (forexDto == null) {
            throw new NullPointerException("forexDto is null");
        }

        return forexDto;
    }

    @Override
    public StockDto getStockBySymbol(String symbol) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STOCK_SERVICE_URL + "/stock/by-symbol/" + symbol))
//                .header("Authorization", SpringSecurityUtil.getJwtToken())
                .GET()
                .build();

        HttpResponse<String> response;
        StockDto stockDto;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonUserListing = response.body();


            stockDto = objectMapper.readValue(jsonUserListing, StockDto.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException("Stock not found");
        }

        if (stockDto == null) {
            throw new NullPointerException("stockDto is null");
        }

        return stockDto;
    }

    @Override
    public Object getSecuritiesByOrder(Order order) {
        return switch (order.getListingType()) {
            case STOCK -> getStockBySymbol(order.getListingSymbol());
            case FOREX -> getForexBySymbol(order.getListingSymbol());
            case OPTION -> getOptionBySymbol(order.getListingSymbol());
            case FUTURE -> getFuturesContractBySymbol(order.getListingSymbol());
            default -> null;
        };
    }

    private FuturesContractDto getFuturesContractBySymbol(String listingSymbol) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STOCK_SERVICE_URL + "/futures/name/" + UriEncoder.encode(listingSymbol)))
//                .header("Authorization", SpringSecurityUtil.getJwtToken())
                .GET()
                .build();

        HttpResponse<String> response;
        FuturesContractDto futuresContractDto;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonUserListing = response.body();

            System.out.println(response.body());
            futuresContractDto = objectMapper.readValue(jsonUserListing, FuturesContractDto.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException("Future Contract not found");
        }

        if (futuresContractDto == null) {
            throw new NullPointerException("futureContractDto is null");
        }

        return futuresContractDto;
    }

    private OptionDto getOptionBySymbol(String listingSymbol) {
        return OptionDto.fromString(listingSymbol);
    }

    @Override
    public ExchangeDto getExchangeExchangeAcronym(String exchangeAcronym) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STOCK_SERVICE_URL + "/exchange/exchange-name?exchange=" + URLEncoder.encode(exchangeAcronym, StandardCharsets.UTF_8)))
                //        .header("Authorization", SpringSecurityUtil.getJwtToken())
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        ExchangeDto exchangeDto;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.body().isEmpty()) {
                exchangeDto = new ExchangeDto();
                exchangeDto.setExchangeAcronym(exchangeAcronym);
                exchangeDto.setCurrency("USD");
                exchangeDto.setTimeZone(0);
                return exchangeDto;
            }
            String jsonUserListing = response.body();
            exchangeDto = objectMapper.readValue(jsonUserListing, ExchangeDto.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException("Exchange not found");
        }

        if (exchangeDto == null) {
            throw new NullPointerException("exchangeDto is null");
        }

        return exchangeDto;
    }
}
