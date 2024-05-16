package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecuritiesOwnershipDto {
    private Long id;
    private String email;
    private boolean ownedByBank = false;
    private String accountNumber;
    private String securitiesSymbol;
    private Integer quantity;
    private Integer quantityOfPubliclyAvailable;
    private Integer reservedQuantity;
}
