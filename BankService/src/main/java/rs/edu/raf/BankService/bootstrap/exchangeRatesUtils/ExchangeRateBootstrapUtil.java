package rs.edu.raf.BankService.bootstrap.exchangeRatesUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeRateBootstrapUtil {
    //GET https://v6.exchangerate-api.com/v6/YOUR-API-KEY/latest/USD
    private static final String api_key = "373353669fbb609262613fc8";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + api_key + "/latest/";
    public static final String[] allowedCurrencies = {"USD", "EUR", "GBP", "CHF", "JPY", "RSD", "CAD", "AUD"};

    public static List<ExchangeRateApiResponse> getDataFromApi() {
        HttpClient client = HttpClient.newHttpClient();
        List<ExchangeRateApiResponse> retVal = new ArrayList<>();
        for (String currency : allowedCurrencies) {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(API_URL + currency)).build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                ObjectMapper objectMapper = new ObjectMapper();
                ExchangeRateApiResponse apiResponse = objectMapper.readValue(response.body(), ExchangeRateApiResponse.class);
                Map<String, Double> allowedConversions = new HashMap<>();
                for (String currency1 : allowedCurrencies) {
                    allowedConversions.put(currency1, apiResponse.getConversion_rates().get(currency1));
                }
                apiResponse.setConversion_rates(allowedConversions);
                retVal.add(apiResponse);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return retVal;
    }


}
