package rs.edu.raf.OTCService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.OTCService.data.enums.ListingType;
import rs.edu.raf.OTCService.data.enums.OrderActionType;
import rs.edu.raf.OTCService.data.enums.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private OrderStatus orderStatus;

    private OrderActionType orderActionType;

    private ListingType listingType;

    private String securitiesSymbol;

    private Integer quantity;

    private Long settlementDate = -1L;

    private double limitPrice = -1;

    private double stopPrice = -1;

    private boolean allOrNone = false;

    private boolean margin = false;

}

