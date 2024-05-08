package rs.edu.raf.BankService.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.BankService.data.dto.AgentDto;
import rs.edu.raf.BankService.service.IAMService;
import rs.edu.raf.BankService.springSecurityUtil.SpringSecurityUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class IAMServiceImpl implements IAMService {

    @Value("${iam.service.url}")
    private String IAM_SERVICE_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    @Retryable(
            retryFor = {IOException.class, SocketTimeoutException.class, SocketTimeoutException.class, HttpConnectTimeoutException.class, InterruptedException.class},
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000)
    )
    public Double getAgentLimitLeft(Long agentId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IAM_SERVICE_URL + "/" + agentId))
                .header("Authorization", SpringSecurityUtil.getAuthorizationHeader())
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        AgentDto agent;
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonUserListing = response.body();
            agent = objectMapper.readValue(jsonUserListing, AgentDto.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return agent.getLeftOfLimit();
    }

    @Override
    @Retryable(
            retryFor = {IOException.class, SocketTimeoutException.class, SocketTimeoutException.class, HttpConnectTimeoutException.class, InterruptedException.class},
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000)
    )
    public boolean reduceAgentLimit(Long agentId, Double amount) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IAM_SERVICE_URL + "/reduce-daily-limit?agentId="+agentId+"&amount="+amount))
                .method("PUT", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        Boolean answer;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonUserListing = response.body();
            answer = objectMapper.readValue(jsonUserListing, Boolean.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.statusCode() == HttpStatus.OK.value() && answer != null && answer;
    }

    @Override
    @Retryable(
            retryFor = {IOException.class, SocketTimeoutException.class, SocketTimeoutException.class, HttpConnectTimeoutException.class, InterruptedException.class},
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000)
    )
    public Boolean isApprovalNeeded(Long agentId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IAM_SERVICE_URL + "/" + agentId))
                .header("Authorization", SpringSecurityUtil.getAuthorizationHeader())
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        AgentDto agent;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonUserListing = response.body();
            agent = objectMapper.readValue(jsonUserListing, AgentDto.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException("Agent not found");
        }

        if(agent == null) {
            throw new NullPointerException("Agent is null");
        }

        return agent.getOrderApprovalRequired();
    }

    @Override
    public Boolean getUserById(Long id) {
        return null;
    }
}

