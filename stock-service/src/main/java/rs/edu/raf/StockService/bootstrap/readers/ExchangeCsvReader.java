package rs.edu.raf.StockService.bootstrap.readers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import rs.edu.raf.StockService.data.entities.Exchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class ExchangeCsvReader {
    //  private static final String FILE_PATH = "StockService/src/main/resources/csvs/exchanges.csv";
    private BufferedReader reader;
    private Resource resource;

    public ExchangeCsvReader(ResourceLoader resourceLoader) {
        resource = resourceLoader.getResource("classpath:csvs/exchanges.csv");
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    private BufferedReader initBufferedReaderStream() {
        try {
            if (this.reader != null) {
                return reader;
            }
            reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return reader;
    }

    public List<Exchange> loadExchangeCsv() {
        List<Exchange> data = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";
        long i = 0;
        boolean isFirst = true;

        try {
            initBufferedReaderStream();

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);

                TimeZone timeZone = TimeZone.getTimeZone(row[5]);
                ZonedDateTime currentTime = ZonedDateTime.now(timeZone.toZoneId());
                ZonedDateTime currentTimeInUTC = ZonedDateTime.now(ZoneId.of("UTC"));
                Integer timeZoneDiff = currentTime.getHour() - currentTimeInUTC.getHour();

                Exchange exchange = new Exchange(
                        row[0],
                        row[1],
                        row[2],
                        row[3],
                        row[4],
                        timeZoneDiff
                );

                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                data.add(exchange);
                exchange.setId(i);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
