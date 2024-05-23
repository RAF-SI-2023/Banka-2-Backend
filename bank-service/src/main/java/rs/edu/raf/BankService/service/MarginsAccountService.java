package rs.edu.raf.BankService.service;

import rs.edu.raf.BankService.data.dto.MarginsAccountDto;

public interface MarginsAccountService {

    MarginsAccountDto createMarginsAccount(MarginsAccountDto marginsAccountDto);
    MarginsAccountDto updateMarginsAccount(Long id, MarginsAccountDto marginsAccountDto);
    void deleteById(Long id);
    MarginsAccountDto findById(Long id);
}
