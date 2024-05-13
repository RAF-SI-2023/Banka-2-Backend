package rs.edu.raf.OTCService.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.dto.GenericTransactionDto;
import rs.edu.raf.OTCService.data.enums.TransactionStatus;
import rs.edu.raf.OTCService.util.SpringSecurityUtil;
import rs.edu.raf.OTCService.service.BankService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    @Value("${bank.service.url}")
    private String BANK_SERVICE_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Boolean createTransaction(ContractDto contractDto) {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(BANK_SERVICE_URL + "/transaction/securities"))
                    .header("Authorization", SpringSecurityUtil.getAuthorizationHeader())
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(contractDto)))
                    .build();
        } catch (JsonProcessingException e) {
            System.out.println("PUCA OVDE");
            throw new RuntimeException(e);
        }
        try {
            System.out.println(objectMapper.writeValueAsString(contractDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(request.bodyPublisher().get());
        System.out.println(request);
        Boolean isSent = false;
        try {
            System.out.println("ILI OVDE");
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            GenericTransactionDto res = objectMapper.readValue(response.body(), GenericTransactionDto.class);
            if (res.getStatus() == TransactionStatus.CONFIRMED)
                isSent = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return isSent;
    }
}
