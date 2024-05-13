package rs.edu.raf.OTCService.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.util.SpringSecurityUtil;
import rs.edu.raf.OTCService.data.dto.OrderDto;
import rs.edu.raf.OTCService.service.BankService;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    @Value("${bank.service.url}")
    private String BANK_SERVICE_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Retryable(
            retryFor = {IOException.class, SocketTimeoutException.class, SocketTimeoutException.class, HttpConnectTimeoutException.class, InterruptedException.class},
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000)
    )
    public Boolean createTransaction(ContractDto contractDto) {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(BANK_SERVICE_URL + "/transaction/securities"))
                    .header("Authorization", SpringSecurityUtil.getAuthorizationHeader())
                    .method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(contractDto)))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Boolean isSent = false;
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonUserListing = response.body();
            isSent = objectMapper.readValue(jsonUserListing, Boolean.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return isSent;
    }
}
