package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.entities.credit.Credit;
import rs.edu.raf.BankService.data.entities.credit.CreditRequest;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;

@Component
public class CreditMapper {
    public Credit creditDtoToCredit(CreditDto dto) {
        return new Credit(
                dto.getCreditName(),
                dto.getCreditNumber(),
                dto.getCreditAmount(),
                dto.getPaymentPeriodMonths(),
                dto.getNominalInterestRate(),
                dto.getEffectiveInterestRate(),
                dto.getCreditCreationDate(),
                dto.getCreditExpirationDate(),
                dto.getInstallmentAmount(),
                dto.getNextInstallmentDate(),
                dto.getRemainingAmount(),
                dto.getCurrencyCode(),
                dto.getAccountNumber());
    }

    public CreditDto creditToCreditDto(Credit credit) {
        return new CreditDto(
                credit.getCreditName(),
                credit.getCreditNumber(),
                credit.getCreditAmount(),
                credit.getPaymentPeriodMonths(),
                credit.getNominalInterestRate(),
                credit.getEffectiveInterestRate(),
                credit.getCreditCreationDate(),
                credit.getCreditExpirationDate(),
                credit.getInstallmentAmount(),
                credit.getNextInstallmentDate(),
                credit.getRemainingAmount(),
                credit.getCurrencyCode(),
                credit.getAccountNumber());

    }

    public CreditRequest creditRequestDtoToCreditRequest(CreditRequestDto dto) {
        return new CreditRequest(
                dto.getId(),
                CreditRequestStatus.PENDING,
                dto.getCreditType(),
                dto.getCreditAmount(),
                dto.getCreditPurpose(),
                dto.getMonthlySalary(),
                dto.getPermanentEmployment(),
                dto.getEmploymentPeriod(),
                dto.getMaturity(),
                dto.getBranch(),
                dto.getMobileNumber(),
                dto.getAccountNumber(),
                dto.getNote(),
                dto.getCurrency(),
                dto.getEducationLevel(),
                dto.getMaritalStatus(),
                dto.getHousingStatus(),
                dto.getOwnCar(),
                dto.getPaymentPeriodMonths());
    }

    public CreditRequestDto creditRequestToCreditRequestDto(CreditRequest creditRequest) {
        return new CreditRequestDto(
                creditRequest.getId(),
                creditRequest.getStatus(),
                creditRequest.getCreditType(),
                creditRequest.getCreditAmount(),
                creditRequest.getCreditPurpose(),
                creditRequest.getMonthlySalary(),
                creditRequest.getPermanentEmployment(),
                creditRequest.getEmploymentPeriod(),
                creditRequest.getMaturity(),
                creditRequest.getBranch(),
                creditRequest.getMobileNumber(),
                creditRequest.getAccountNumber(),
                creditRequest.getNote(),
                creditRequest.getCurrency(),
                creditRequest.getEducationLevel(),
                creditRequest.getMaritalStatus(),
                creditRequest.getHousingStatus(),
                creditRequest.getOwnCar(),
                creditRequest.getPaymentPeriodMonths());
    }


}
