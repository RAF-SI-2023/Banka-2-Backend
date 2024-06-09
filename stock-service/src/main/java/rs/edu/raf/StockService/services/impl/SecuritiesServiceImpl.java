package rs.edu.raf.StockService.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.mapper.SecuritiesMapper;
import rs.edu.raf.StockService.services.FuturesContractService;
import rs.edu.raf.StockService.services.OptionService;
import rs.edu.raf.StockService.services.SecuritiesService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SecuritiesServiceImpl implements SecuritiesService {
    private final FuturesContractService futuresContractService;

    private final OptionService optionService;

    private final SecuritiesMapper securitiesMapper;

    @Override
    public List<SecuritiesDto> getSecuritiesBySettlementDate(Optional<LocalDate> futureDate) {
        long currentDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long cutoffDate = futureDate
                .map(date -> date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .orElse(Long.MAX_VALUE);

        List<FuturesContractDto> futuresContracts = getFutureContracts(currentDate, cutoffDate);
        List<Option> options = getOptions(currentDate, cutoffDate);

        List<SecuritiesDto> futuresContractDtos = futuresContracts.stream()
                .map(securitiesMapper::convertFuturesContractToSecuritiesDto)
                .toList();

        List<SecuritiesDto> optionDtos = options.stream()
                .map(securitiesMapper::convertOptionToSecuritiesDto)
                .toList();

        return Stream.concat(futuresContractDtos.stream(), optionDtos.stream())
                .sorted(Comparator.comparing(SecuritiesDto::getSettlementDate))
                .collect(Collectors.toList());
    }

    private List<FuturesContractDto> getFutureContracts(long currentDate, long cutoffDate) {
        return futuresContractService.findAll()
                .stream()
                .filter(contract -> contract.getSettlementDate() > currentDate && contract.getSettlementDate() <= cutoffDate)
                .collect(Collectors.toList());
    }

    private List<Option> getOptions(long currentDate, long cutoffDate) {
        return optionService.findAll()
                .stream()
                .filter(option -> option.getSettlementDate() > currentDate && option.getSettlementDate() <= cutoffDate)
                .collect(Collectors.toList());
    }
}
