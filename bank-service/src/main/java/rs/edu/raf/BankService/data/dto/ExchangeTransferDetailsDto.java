package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeTransferDetailsDto {
    private Long id; //broj naloga
    private String fromAccountNumber;
    private String toAccountNumber;
    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double exchangeRate;
    private double fee;
    private double totalAmount;
    private long dateTimeEpoch;
}
