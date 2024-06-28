package rs.edu.raf.OTCService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.dto.SecurityOwnershipDto;
import rs.edu.raf.OTCService.data.dto.testing.MyOfferDto;
import rs.edu.raf.OTCService.data.dto.testing.OfferDto;

import java.util.List;

@Service
public interface BankService {
    Boolean createTransaction(ContractDto contractDto);

    boolean buyBank3Stock(MyOfferDto myOfferDto);

    boolean sellStockToBank3(OfferDto offerDto);

    List<SecurityOwnershipDto> getSecurityOwnerships();
}
