package rs.edu.raf.StockService.bootstrap.readers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.util.Json;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrencyCsvReader {
    public static List<Currency> readCurrencyCsv() {
        BufferedReader br;
        String line;
        String cvsSplitBy = ",";
        List<Currency> currencyList = new ArrayList<>();
        boolean hasHeader = true;
        try {
            br = new BufferedReader(new FileReader("StockService/src/main/resources/csvs/physical_currency_list.csv"));
            while ((line = br.readLine()) != null) {
                if (hasHeader) {
                    hasHeader = false;
                    continue;
                }
                String[] data = line.split(cvsSplitBy);
                //data[0] - currencyCode
                //data[1] - currencyName
                Currency currency = new Currency(data[1], data[0]);
                currencyList.add(currency);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currencyList;
    }

    public static List<Currency> loadCurrencyData() {
        List<Currency> currencyList = readCurrencyCsv();
        int i = 0;
        for (Currency currency : currencyList) {
            String currencySymbol = "";
            Locale locale = null;
            try {
                locale = new Locale("", currency.getCurrencyCode().substring(0, 2));
                currencySymbol = java.util.Currency.getInstance(currency.getCurrencyCode()).getSymbol();
                currency.setCurrencySymbol(currencySymbol);
                currency.setCurrencyPolity(locale.getDisplayCountry());
            } catch (Exception e) {

                if (locale == null) {
                    currency.setCurrencyPolity("Unknown");
                }
                if (currencySymbol.isEmpty()) {
                    currency.setCurrencySymbol(currency.getCurrencyCode());
                }
                continue;
            }
            currency.setCurrencySymbol(currencySymbol);
            currency.setId(i++);
        }
        return currencyList;
    }

    public static List<CurrencyInflation> pullCurrencyInflationData(List<Currency> currencyList) {
        List<CurrencyInflation> currencyInflationListEnd = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<?> response = client.send(HttpRequest.newBuilder().uri(URI.create("https://www.imf.org/external/datamapper/api/v1/PCPIPCH")).build(), HttpResponse.BodyHandlers.ofString());

            JsonNode jsonNode = Json.mapper().readTree(response.body().toString());

            JsonNode nodeContainingInflationData = jsonNode.get("values").get("PCPIPCH");
            for (Currency currency : currencyList) {

                Locale locale = new Locale("", currency.getCurrencyCode().substring(0, 2));
                String ISO3CountryCode = "";
                try {
                    ISO3CountryCode = locale.getISO3Country();
                } catch (Exception e) {
                    if (!currency.getCurrencyCode().equals("EUR")) {
                        System.err.println("Country code not found for currency: " + currency.getCurrencyCode() + " " + currency.getCurrencyName() + " " + currency.getCurrencySymbol() + " " + currency.getCurrencyPolity());
                        continue;
                    }
                }
                if (currency.getCurrencyCode().equals("EUR")) {
                    ISO3CountryCode = "EURO";
                }
                JsonNode currencyInflation = nodeContainingInflationData.get(ISO3CountryCode);
                if (currencyInflation != null) {
                    List<CurrencyInflation> currencyInflationList = new ArrayList<>();
                    currencyInflation.fields().forEachRemaining(inflation -> {
                        CurrencyInflation currencyInflation1 =
                                new CurrencyInflation(inflation.getValue().asDouble(), Long.parseLong(inflation.getKey()), currency.getId());
                        currencyInflationListEnd.add(currencyInflation1);
                        currencyInflationList.add(currencyInflation1);
                    });
                    currency.setInflationList(currencyInflationList);

                }
            }


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return currencyInflationListEnd;
    }

}