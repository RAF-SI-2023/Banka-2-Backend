package rs.edu.raf.BankService.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgentDto {
    private Double userLimit;
    private Double leftOfLimit;
    private Boolean orderApprovalRequired;

}