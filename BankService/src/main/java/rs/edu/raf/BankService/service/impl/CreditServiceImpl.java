package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.entities.Credit;
import rs.edu.raf.BankService.data.entities.CreditRequest;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;
import rs.edu.raf.BankService.mapper.CreditMapper;
import rs.edu.raf.BankService.repository.credit.CreditRepository;
import rs.edu.raf.BankService.repository.credit.CreditRequestRepository;
import rs.edu.raf.BankService.service.CreditService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final CreditMapper creditMapper;

    @Override
    public CreditDto createCredit(Credit credit) {
        //TODO implement something something with requests
        return null;
    }

    @Override
    public List<CreditDto> getCreditsByAccountNumber(String accountNumber) {
        return creditRepository.findAllByAccountNumber(accountNumber).stream().map(creditMapper::creditToCreditDto).toList();
    }

    @Override
    public CreditDto getCreditByCreditNumber(Long creditNumber) {
        return creditMapper.creditToCreditDto(creditRepository.findCreditByCreditNumber(creditNumber));
    }

    @Override
    public CreditRequestDto createCreditRequest(CreditRequestDto creditRequestDto) {
        CreditRequest creditRequest = creditMapper.creditRequestDtoToCreditRequest(creditRequestDto);
        creditRequest = creditRequestRepository.save(creditRequest);
        return creditMapper.creditRequestToCreditRequestDto(creditRequest);
    }

    @Override
    public List<CreditRequestDto> getAllCreditRequests() {
        return creditRequestRepository.findAll().stream().map(creditMapper::creditRequestToCreditRequestDto).toList();
    }

    @Override
    public CreditRequestDto approveCreditRequest(Long creditRequestId, CreditDto creditDto) {
        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId).orElseThrow();
        if (creditRequest.getStatus() != CreditRequestStatus.PENDING) {
            throw new RuntimeException("Credit request is not pending");
        }
        creditRequest.setStatus(CreditRequestStatus.APPROVED);
        creditRequest = creditRequestRepository.save(creditRequest);
        return creditMapper.creditRequestToCreditRequestDto(creditRequest);
    }

}
