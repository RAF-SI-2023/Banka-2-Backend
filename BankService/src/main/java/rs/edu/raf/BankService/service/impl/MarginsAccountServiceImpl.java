package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.MarginsAccountDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;
import rs.edu.raf.BankService.mapper.MarginsAccountMapper;
import rs.edu.raf.BankService.repository.MarginsAccountRepository;
import rs.edu.raf.BankService.service.MarginsAccountService;

@RequiredArgsConstructor
@Service
public class MarginsAccountServiceImpl implements MarginsAccountService {

    private final MarginsAccountRepository marginsAccountRepository;
    private final MarginsAccountMapper marginsAccountMapper;

    @Override
    public MarginsAccountDto createMarginsAccount(MarginsAccountDto marginsAccountDto) {
        MarginsAccount marginsAccount = marginsAccountMapper.toEntity(marginsAccountDto);

        MarginsAccount savedMarginsAccount = marginsAccountRepository.save(marginsAccount);

        return marginsAccountMapper.toDto(savedMarginsAccount);
    }

    @Override
    public MarginsAccountDto updateMarginsAccount(Long id, MarginsAccountDto marginsAccountDto) {
        boolean isPresent = marginsAccountRepository.existsById(id);

        if (isPresent) {
            MarginsAccount updatedMarginsAccount =
                    marginsAccountRepository.save(marginsAccountMapper.toEntity(marginsAccountDto));
            return marginsAccountMapper.toDto(updatedMarginsAccount);
        }
        else {
            throw new RuntimeException("Margins account with id " + id + " doesn't exist");
        }
    }

    @Override
    public void deleteById(Long id) {
        boolean isPresent = marginsAccountRepository.existsById(id);

        if (isPresent) {
            marginsAccountRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("Margins account with id " + id + " doesn't exist");
        }
    }

    @Override
    public MarginsAccountDto findById(Long id) {
        MarginsAccount marginsAccount = marginsAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Margins account with id " + id + " doesn't exist"));

        return marginsAccountMapper.toDto(marginsAccount);
    }
}
