package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.ListingType;

@Entity
@Table(name = "securities_ownerships")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecuritiesOwnership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ListingType listingType = ListingType.STOCK;

    private String email;

    private boolean ownedByBank = false;

    private String accountNumber;

    private String securitiesSymbol;

    private Integer quantity;

    private Integer quantityOfPubliclyAvailable;

    private Integer reservedQuantity;

    private Double averageBuyingPrice;

}
