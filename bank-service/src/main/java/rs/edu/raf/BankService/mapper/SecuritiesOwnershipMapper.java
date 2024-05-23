package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;

@Component
public class SecuritiesOwnershipMapper {

    public SecuritiesOwnership fromDto(SecuritiesOwnershipDto dto) {
        return new SecuritiesOwnership(
                dto.getId(),
                dto.getEmail(),
                dto.isOwnedByBank(),
                dto.getAccountNumber(),
                dto.getSecuritiesSymbol(),
                dto.getQuantity(),
                dto.getQuantityOfPubliclyAvailable(),
                dto.getReservedQuantity()
        );
    }

    public SecuritiesOwnershipDto toDto(SecuritiesOwnership so) {
        return new SecuritiesOwnershipDto(
                so.getId(),
                so.getEmail(),
                so.isOwnedByBank(),
                so.getAccountNumber(),
                so.getSecuritiesSymbol(),
                so.getQuantity(),
                so.getQuantityOfPubliclyAvailable(),
                so.getReservedQuantity()
        );
    }
}
