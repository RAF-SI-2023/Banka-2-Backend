package rs.edu.raf.BankService.mapper;


import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.ActionAgentProfitDto;
import rs.edu.raf.BankService.data.dto.TotalActionAgentProfitDto;
import rs.edu.raf.BankService.data.entities.profit.ActionAgentProfit;

@Component
public class ActionAgentProfitMapper {

    public ActionAgentProfitDto actionAgentProfitToActionAgentProfitDto(ActionAgentProfit actionAgentProfit) {
        return new ActionAgentProfitDto(
                actionAgentProfit.getId(),
                actionAgentProfit.getUserEmail(),
                actionAgentProfit.getProfit(),
                actionAgentProfit.getTransactionType(),
                actionAgentProfit.getTransactionId(),
                actionAgentProfit.getCreatedAt()
        );
    }

    public ActionAgentProfit actionAgentProfitDtoToActionAgentProfit(ActionAgentProfitDto actionAgentProfitDto) {
        return new ActionAgentProfit(
                actionAgentProfitDto.getId(),
                actionAgentProfitDto.getUserEmail(),
                actionAgentProfitDto.getProfit(),
                actionAgentProfitDto.getTransactionType(),
                actionAgentProfitDto.getTransactionId(),
                actionAgentProfitDto.getCreatedAt()
        );
    }

    public TotalActionAgentProfitDto actionAgentProfitToTotalActionAgentProfitDto(ActionAgentProfit actionAgentProfit) {
        return new TotalActionAgentProfitDto(
                actionAgentProfit.getUserEmail(),
                actionAgentProfit.getProfit()
        );
    }
}
