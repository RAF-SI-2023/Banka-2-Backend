package rs.edu.raf.BankService.service;

import rs.edu.raf.BankService.data.dto.MarginsAccountRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsAccountResponseDto;

import java.util.List;

public interface MarginsAccountService {

    MarginsAccountResponseDto createMarginsAccount(MarginsAccountRequestDto marginsAccountRequestDto);

    MarginsAccountResponseDto updateMarginsAccount(Long id, MarginsAccountRequestDto marginsAccountRequestDto);

    void deleteById(Long id);

    List<MarginsAccountResponseDto> findById(Long id);

    List<MarginsAccountResponseDto> findByUserId(Long userId);

    MarginsAccountResponseDto settleMarginCall(Long id, Double deposit);

    List<MarginsAccountResponseDto> findByEmail(String email);

    List<MarginsAccountResponseDto> findByAccountNumber(String accountNumber);
}
