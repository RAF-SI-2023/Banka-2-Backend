package rs.edu.raf.BankService.service;

import rs.edu.raf.BankService.data.dto.MarginsAccountRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsAccountResponseDto;

public interface MarginsAccountService {

    MarginsAccountResponseDto createMarginsAccount(MarginsAccountRequestDto marginsAccountRequestDto);
    MarginsAccountResponseDto updateMarginsAccount(Long id, MarginsAccountRequestDto marginsAccountRequestDto);
    void deleteById(Long id);
    MarginsAccountResponseDto findById(Long id);
    MarginsAccountResponseDto findByUserId(Long userId);
    MarginsAccountResponseDto settleMarginCall(Long id, Double deposit);
    MarginsAccountResponseDto findByEmail(String email);
    MarginsAccountResponseDto findByAccountNumber(String accountNumber);
}
