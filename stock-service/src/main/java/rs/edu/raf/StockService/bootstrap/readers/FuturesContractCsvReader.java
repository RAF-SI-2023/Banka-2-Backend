package rs.edu.raf.StockService.bootstrap.readers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.data.enums.FuturesContractType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FuturesContractCsvReader {
    private Resource resource;

    public FuturesContractCsvReader(ResourceLoader resourceLoader) {
        resource = resourceLoader.getResource("classpath:csvs/future_data.csv");
    }

    public List<FuturesContract> readFromFile() {
        List<FuturesContract> futuresContracts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            long id = 0;
            String line = reader.readLine(); // Read header column
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                futuresContracts.add(
                        new FuturesContract(
                                ++id,
                                data[0],
                                "", // Code missing
                                Integer.parseInt(data[1]),
                                data[2],
                                0,
                                System.currentTimeMillis(),
                                Integer.parseInt(data[3]),
                                FuturesContractType.valueOf(data[4]),
                                Double.parseDouble(data[5]),
                                Double.parseDouble(data[6])
                        )
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return futuresContracts;
    }


}
