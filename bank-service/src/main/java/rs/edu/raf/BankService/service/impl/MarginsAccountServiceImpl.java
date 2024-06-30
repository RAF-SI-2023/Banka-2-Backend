package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.BankTransferTransactionDetailsDto;
import rs.edu.raf.BankService.data.dto.MarginsAccountRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsAccountResponseDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;
import rs.edu.raf.BankService.data.enums.TransactionDirection;
import rs.edu.raf.BankService.mapper.MarginsAccountMapper;
import rs.edu.raf.BankService.repository.MarginsAccountRepository;
import rs.edu.raf.BankService.repository.MarginsTransactionRepository;
import rs.edu.raf.BankService.service.MarginsAccountService;
import rs.edu.raf.BankService.springSecurityUtil.SpringSecurityUtil;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MarginsAccountServiceImpl implements MarginsAccountService {

    private final MarginsAccountRepository marginsAccountRepository;
    private final MarginsAccountMapper marginsAccountMapper;
    private final MarginsTransactionRepository marginsTransactionRepository;

    @Override
    public MarginsAccountResponseDto createMarginsAccount(MarginsAccountRequestDto marginsAccountRequestDto) {
        MarginsAccount marginsAccount = marginsAccountMapper.toEntity(marginsAccountRequestDto);

        MarginsAccount savedMarginsAccount = marginsAccountRepository.save(marginsAccount);

        return marginsAccountMapper.toDto(savedMarginsAccount);
    }

    @Override
    public MarginsAccountResponseDto updateMarginsAccount(Long id, MarginsAccountRequestDto marginsAccountRequestDto) {
        boolean isPresent = marginsAccountRepository.existsById(id);

        if (isPresent) {
            MarginsAccount updatedMarginsAccount =
                    marginsAccountRepository.save(marginsAccountMapper.toEntity(marginsAccountRequestDto));
            return marginsAccountMapper.toDto(updatedMarginsAccount);
        } else {
            throw new RuntimeException("Margins account with id " + id + " doesn't exist");
        }
    }

    @Override
    public void deleteById(Long id) {
        boolean isPresent = marginsAccountRepository.existsById(id);

        if (isPresent) {
            marginsAccountRepository.deleteById(id);
        } else {
            throw new RuntimeException("Margins account with id " + id + " doesn't exist");
        }
    }

    @Override
    public List<MarginsAccountResponseDto> findById(Long id) {
        List<MarginsAccount> marginsAccounts = marginsAccountRepository.findAllById(id);

        return marginsAccounts.stream()
                .map(marginsAccountMapper::toDto)
                .toList();

    }

    @Override
    public List<MarginsAccountResponseDto> findByUserId(Long userId) {
        List<MarginsAccount> marginsAccounts = marginsAccountRepository.findAllByUserId(userId);

            return marginsAccounts.stream()
                    .map(marginsAccountMapper::toDto)
                    .toList();
    }

    // ovde na frontu ako margin call nije promenjen, onda moze da ispise da je neuspela akcija
    @Override
    public MarginsAccountResponseDto settleMarginCall(Long id, Double deposit) {
        MarginsAccount marginsAccount = marginsAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Margins account with id " + id + " doesn't exist"));

        if (!marginsAccount.isMarginCall()) {
            throw new RuntimeException("Margin call for margins account with id " + id + " already settled");
        }

        Double currentMinus = marginsAccount.getMaintenanceMargin() - marginsAccount.getBalance();

        if (deposit >= currentMinus) {
            Double extraAmount = deposit - currentMinus;
            marginsAccount.setBalance(marginsAccount.getBalance() + extraAmount);
            marginsAccount.setMarginCall(false);
            marginsTransactionRepository.save(createTransactionForMarginCallSettlement(deposit));
            MarginsAccount updatedAccount = marginsAccountRepository.save(marginsAccount);

            return marginsAccountMapper.toDto(updatedAccount);
        } else {
            return marginsAccountMapper.toDto(marginsAccount);
        }
    }

    @Override
    public List<MarginsAccountResponseDto> findByEmail(String email) {
        List<MarginsAccount> marginsAccounts = marginsAccountRepository.findAllByEmail(email);

            return marginsAccounts.stream()
                    .map(marginsAccountMapper::toDto)
                    .toList();
    }


    @Override
    public List<MarginsAccountResponseDto> findByAccountNumber(String accountNumber) {
        List<MarginsAccount> marginsAccounts = marginsAccountRepository.findAllByAccountNumber(accountNumber);

                return marginsAccounts.stream()
                        .map(marginsAccountMapper::toDto)
                        .toList();
    }

    public MarginsTransaction createTransactionForMarginCallSettlement(Double deposit) {
        MarginsTransaction marginsTransaction = new MarginsTransaction();
        marginsTransaction.setInvestmentAmount(deposit);
        marginsTransaction.setCreatedAt(System.currentTimeMillis());
        marginsTransaction.setUserId(SpringSecurityUtil.getPrincipalId());
        marginsTransaction.setType(TransactionDirection.DEPOSIT);
        marginsTransaction.setDescription("MARGIN CALL DEPOSIT " + deposit);

        return marginsTransaction;
    }
}
