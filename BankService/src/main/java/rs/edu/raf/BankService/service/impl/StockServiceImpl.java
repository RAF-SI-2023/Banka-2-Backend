package rs.edu.raf.BankService.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.BankService.data.dto.ExchangeDto;
import rs.edu.raf.BankService.data.dto.ForexDto;
import rs.edu.raf.BankService.data.dto.ListingDto;
import rs.edu.raf.BankService.data.dto.StockDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.service.StockService;
import rs.edu.raf.BankService.springSecurityUtil.SpringSecurityUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class StockServiceImpl implements StockService {

    @Value("${stock.service.url}")
    private String STOCK_SERVICE_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public ForexDto getForexById(Long id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STOCK_SERVICE_URL + "/forex/id/" + id))
                .header("Authorization", SpringSecurityUtil.getJwtToken())
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

        if(response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException("Forex not found");
        }

        if(forexDto == null) {
            throw new NullPointerException("forexDto is null");
        }

        return forexDto;
    }

    @Override
    public StockDto getStockById(Long id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STOCK_SERVICE_URL + "/stock/id/" + id))
                .header("Authorization", SpringSecurityUtil.getJwtToken())
                .method("GET", HttpRequest.BodyPublishers.noBody())
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

        if(response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException("Stock not found");
        }

        if(stockDto == null) {
            throw new NullPointerException("stockDto is null");
        }

        return stockDto;
    }

    @Override
    public ListingDto getSecuritiesByOrder(Order order) {
        return switch (order.getListingType()) {
            case STOCK -> getStockById(order.getListingId());
            case FOREX -> getForexById(order.getListingId());
            default -> null;
        };
    }

    @Override
    public ExchangeDto getExchangeExchangeAcronym(String exchangeAcronym) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STOCK_SERVICE_URL + "/exchange/exchange-acronym/" + exchangeAcronym))
                .header("Authorization", SpringSecurityUtil.getJwtToken())
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        ExchangeDto exchangeDto;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonUserListing = response.body();
            exchangeDto = objectMapper.readValue(jsonUserListing, ExchangeDto.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException("Exchange not found");
        }

        if(exchangeDto == null) {
            throw new NullPointerException("exchangeDto is null");
        }

        return exchangeDto;
    }
}
