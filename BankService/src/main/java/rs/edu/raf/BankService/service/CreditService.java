package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.entities.credit.Credit;

import java.util.List;

@Service
public interface CreditService {
    CreditDto createCredit(Credit credit);

    List<CreditDto> getCreditsByAccountNumber(String accountNumber);

    CreditDto getCreditByCreditNumber(Long creditNumber);

    CreditRequestDto createCreditRequest(CreditRequestDto creditRequestDto);

    List<CreditRequestDto> getAllCreditRequests();

    CreditDto approveCreditRequest(Long creditRequestId);

    Boolean denyCreditRequest(Long creditRequestId);
}
