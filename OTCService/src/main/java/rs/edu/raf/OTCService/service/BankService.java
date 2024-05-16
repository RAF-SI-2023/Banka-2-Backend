package rs.edu.raf.OTCService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.dto.OrderDto;

@Service
public interface BankService {
    Boolean createTransaction(ContractDto contractDto);
}
