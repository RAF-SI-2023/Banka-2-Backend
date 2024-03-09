package rs.edu.raf.StockService.bootstrap.readers;

import rs.edu.raf.StockService.data.entities.Currency;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrencyCsvReader {
    public static List<Currency> readCurrencyCsv() {
        BufferedReader br = null;
        String line = "";
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
                if (currencySymbol.equals("")) {
                    currency.setCurrencySymbol(currency.getCurrencyCode());
                }
                continue;
            }
            currency.setCurrencySymbol(currencySymbol);
            currency.setId(i++);
        }
        return currencyList;
    }

}