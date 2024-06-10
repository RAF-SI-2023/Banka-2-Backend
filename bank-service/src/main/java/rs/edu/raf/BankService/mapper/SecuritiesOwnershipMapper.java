package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.enums.ListingType;

@Component
public class SecuritiesOwnershipMapper {

    public SecuritiesOwnership fromDto(SecuritiesOwnershipDto dto) {
        return new SecuritiesOwnership(
                dto.getId(),
                ListingType.valueOf(dto.getListingType()),
                dto.getEmail(),
                dto.isOwnedByBank(),
                dto.getAccountNumber(),
                dto.getSecuritiesSymbol(),
                dto.getQuantity(),
                dto.getQuantityOfPubliclyAvailable(),
                dto.getReservedQuantity(),
                dto.getAverageBuyingPrice()
        );
    }

    public SecuritiesOwnershipDto toDto(SecuritiesOwnership so) {
        return new SecuritiesOwnershipDto(
                so.getId(),
                so.getListingType().label,
                so.getEmail(),
                so.isOwnedByBank(),
                so.getAccountNumber(),
                so.getSecuritiesSymbol(),
                so.getQuantity(),
                so.getQuantityOfPubliclyAvailable(),
                so.getReservedQuantity(),
                so.getAverageBuyingPrice()
        );
    }
}
