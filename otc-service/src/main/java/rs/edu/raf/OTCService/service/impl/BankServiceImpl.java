package rs.edu.raf.OTCService.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.dto.GenericTransactionDto;
import rs.edu.raf.OTCService.data.dto.testing.MyOfferDto;
import rs.edu.raf.OTCService.data.dto.testing.OfferDto;
import rs.edu.raf.OTCService.data.enums.TransactionStatus;
import rs.edu.raf.OTCService.service.BankService;
import rs.edu.raf.OTCService.util.SpringSecurityUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${bank.service.url:http://bank-service:8003/api}")
    private String BANK_SERVICE_URL;

    @Override
    public Boolean createTransaction(ContractDto contractDto) {
        HttpRequest request = null;
        System.out.println("ULAZI OVDE");
        try {
            System.out.println(contractDto);
            System.out.println(objectMapper.writeValueAsString(contractDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(BANK_SERVICE_URL + "/transaction/securities"))
                    .header("Authorization", SpringSecurityUtil.getAuthorizationHeader())
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(contractDto)))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }


        Boolean isSent = false;
        try {

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            GenericTransactionDto res = objectMapper.readValue(response.body(), GenericTransactionDto.class);
            if (res.getStatus() == TransactionStatus.CONFIRMED)
                isSent = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return isSent;
    }

    @Override
    public boolean buyBank3Stock(MyOfferDto myOfferDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BANK_SERVICE_URL + "/otc-transaction/buy-stock";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", SpringSecurityUtil.getAuthorizationHeader());
        headers.set("Content-Type", "application/json");
        HttpEntity<MyOfferDto> requestEntity = new HttpEntity<>(myOfferDto, headers);
        ResponseEntity<GenericTransactionDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, GenericTransactionDto.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(responseEntity.getBody()).getStatus() == TransactionStatus.CONFIRMED;
        }
        return false;
    }

    @Override
    public boolean sellStockToBank3(OfferDto offerDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BANK_SERVICE_URL + "/otc-transaction/sell-stock";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", SpringSecurityUtil.getAuthorizationHeader());
        headers.set("Content-Type", "application/json");
        HttpEntity<OfferDto> requestEntity = new HttpEntity<>(offerDto, headers);
        ResponseEntity<GenericTransactionDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, GenericTransactionDto.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(responseEntity.getBody()).getStatus() == TransactionStatus.CONFIRMED;
        }
        return false;
    }
}
