package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRequestDto {
    private String fromAccount;
    private String fromCurrency;
    private String toAccount;
    private String toCurrency;
    private double amount;
}
