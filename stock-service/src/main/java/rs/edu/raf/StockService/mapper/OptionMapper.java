package rs.edu.raf.StockService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.OptionDto;
import rs.edu.raf.StockService.data.entities.Option;

@Component
public class OptionMapper {
    public OptionDto optionToOptionDto(Option option) {
        return new OptionDto(
                option.getStockListing(),
                option.getOptionType(),
                option.getStrikePrice(),
                option.getImpliedVolatility(),
                option.getOpenInterest(),
                option.getSettlementDate()
        );
    }

    public Option optionDtoToOption(OptionDto optionDto) {
        return new Option(
                optionDto.getStockListing(),
                optionDto.getOptionType(),
                optionDto.getStrikePrice(),
                optionDto.getImpliedVolatility(),
                optionDto.getOpenInterest(),
                optionDto.getSettlementDate()
        );
    }

}
