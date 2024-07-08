package rs.edu.raf.OTCService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityOwnershipDto {
    private Long id;
    private String listingType;
    private String email;
    private boolean ownedByBank = false;
    private String accountNumber;
    private String securitiesSymbol;
    private Integer quantity;
    private Integer quantityOfPubliclyAvailable;
    private Integer reservedQuantity;
    private Double averageBuyingPrice;
}
