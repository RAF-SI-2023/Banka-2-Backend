package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.BankProfitDto;
import rs.edu.raf.BankService.data.entities.profit.BankProfit;

@Component
public class BankProfitMapper {

    public BankProfitDto bankProfitToBankProfitDto(BankProfit bankProfit) {
        return new BankProfitDto(
                bankProfit.getId(),
                bankProfit.getProfit()
        );
    }

    public BankProfit bankProfitDtoToBankProfit(BankProfitDto bankProfitDto) {
        return new BankProfit(
                bankProfitDto.getId(),
                bankProfitDto.getProfit()
        );
    }
}
