package rs.edu.raf.BankService.unit.creditTests;

import org.junit.jupiter.api.Test;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.entities.credit.Credit;
import rs.edu.raf.BankService.data.entities.credit.CreditRequest;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;
import rs.edu.raf.BankService.data.enums.CreditType;
import rs.edu.raf.BankService.mapper.CreditMapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditMapperTests {

    private final CreditMapper creditMapper = new CreditMapper();

    @Test
    void testCreditDtoToCredit() {
        // Given
        CreditDto creditDto = new CreditDto();
        creditDto.setCreditName("Test Credit");
        creditDto.setCreditNumber(123L);
        creditDto.setCreditAmount(1000.0);
        creditDto.setPaymentPeriodMonths(12L);
        creditDto.setNominalInterestRate(5.0);
        creditDto.setEffectiveInterestRate(5.5);
        creditDto.setCreditCreationDate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        creditDto.setCreditExpirationDate(LocalDateTime.now().plusMonths(12).toEpochSecond(ZoneOffset.UTC));
        creditDto.setInstallmentAmount(100.0);
        creditDto.setNextInstallmentDate(LocalDateTime.now().plusMonths(1).toEpochSecond(ZoneOffset.UTC));
        creditDto.setRemainingAmount(900.0);
        creditDto.setCurrencyCode("USD");
        creditDto.setAccountNumber("123456789");

        // When
        Credit credit = creditMapper.creditDtoToCredit(creditDto);

        // Then
        assertEquals(creditDto.getCreditName(), credit.getCreditName());
        assertEquals(creditDto.getCreditNumber(), credit.getCreditNumber());
        assertEquals(creditDto.getCreditAmount(), credit.getCreditAmount());
        assertEquals(creditDto.getPaymentPeriodMonths(), credit.getPaymentPeriodMonths());
        assertEquals(creditDto.getNominalInterestRate(), credit.getNominalInterestRate());
        assertEquals(creditDto.getEffectiveInterestRate(), credit.getEffectiveInterestRate());
        assertEquals(creditDto.getCreditCreationDate(), credit.getCreditCreationDate());
        assertEquals(creditDto.getCreditExpirationDate(), credit.getCreditExpirationDate());
        assertEquals(creditDto.getInstallmentAmount(), credit.getInstallmentAmount());
        assertEquals(creditDto.getNextInstallmentDate(), credit.getNextInstallmentDate());
        assertEquals(creditDto.getRemainingAmount(), credit.getRemainingAmount());
        assertEquals(creditDto.getCurrencyCode(), credit.getCurrencyCode());
        assertEquals(creditDto.getAccountNumber(), credit.getAccountNumber());
    }

    @Test
    void testCreditToCreditDto() {
        // Given
        Credit credit = new Credit();
        credit.setCreditName("Test Credit");
        credit.setCreditNumber(123L);
        credit.setCreditAmount(1000.0);
        credit.setPaymentPeriodMonths(12L);
        credit.setNominalInterestRate(5.0);
        credit.setEffectiveInterestRate(5.5);
        credit.setCreditCreationDate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        credit.setCreditExpirationDate(LocalDateTime.now().plusMonths(12).toEpochSecond(ZoneOffset.UTC));
        credit.setInstallmentAmount(100.0);
        credit.setNextInstallmentDate(LocalDateTime.now().plusMonths(1).toEpochSecond(ZoneOffset.UTC));
        credit.setRemainingAmount(900.0);
        credit.setCurrencyCode("USD");
        credit.setAccountNumber("123456789");

        // When
        CreditDto creditDto = creditMapper.creditToCreditDto(credit);

        // Then
        assertEquals(credit.getCreditName(), creditDto.getCreditName());
        assertEquals(credit.getCreditNumber(), creditDto.getCreditNumber());
        assertEquals(credit.getCreditAmount(), creditDto.getCreditAmount());
        assertEquals(credit.getPaymentPeriodMonths(), creditDto.getPaymentPeriodMonths());
        assertEquals(credit.getNominalInterestRate(), creditDto.getNominalInterestRate());
        assertEquals(credit.getEffectiveInterestRate(), creditDto.getEffectiveInterestRate());
        assertEquals(credit.getCreditCreationDate(), creditDto.getCreditCreationDate());
        assertEquals(credit.getCreditExpirationDate(), creditDto.getCreditExpirationDate());
        assertEquals(credit.getInstallmentAmount(), creditDto.getInstallmentAmount());
        assertEquals(credit.getNextInstallmentDate(), creditDto.getNextInstallmentDate());
        assertEquals(credit.getRemainingAmount(), creditDto.getRemainingAmount());
        assertEquals(credit.getCurrencyCode(), creditDto.getCurrencyCode());
        assertEquals(credit.getAccountNumber(), creditDto.getAccountNumber());
    }

    @Test
    void testCreditRequestDtoToCreditRequest() {
        // Given
        CreditRequestDto creditRequestDto = new CreditRequestDto();
        creditRequestDto.setId(123L);
        creditRequestDto.setStatus(CreditRequestStatus.PENDING);
        creditRequestDto.setCreditType(CreditType.GOTOVINSKI);
        creditRequestDto.setCreditAmount(1000.0);
        creditRequestDto.setCreditPurpose("Test Purpose");
        creditRequestDto.setMonthlySalary(20000L);
        creditRequestDto.setPermanentEmployment(true);
        creditRequestDto.setEmploymentPeriod(12L);
        creditRequestDto.setMaturity(24L);
        creditRequestDto.setBranch("Test Branch");
        creditRequestDto.setMobileNumber("123456789");
        creditRequestDto.setAccountNumber("987654321");
        creditRequestDto.setNote("Test Note");
        creditRequestDto.setCurrency("USD");
        creditRequestDto.setEducationLevel("Test Education Level");
        creditRequestDto.setMaritalStatus("Test Marital Status");
        creditRequestDto.setHousingStatus("Test Housing Status");
        creditRequestDto.setOwnCar(true);
        creditRequestDto.setPaymentPeriodMonths(12L);
        // Set other properties as needed

        // When
        CreditRequest creditRequest = creditMapper.creditRequestDtoToCreditRequest(creditRequestDto);

        // Then
        assertEquals(creditRequestDto.getId(), creditRequest.getId());
        assertEquals(creditRequestDto.getStatus(), creditRequest.getStatus());
        assertEquals(creditRequestDto.getCreditType(), creditRequest.getCreditType());
        assertEquals(creditRequestDto.getCreditAmount(), creditRequest.getCreditAmount());
        assertEquals(creditRequestDto.getCreditPurpose(), creditRequest.getCreditPurpose());
        assertEquals(creditRequestDto.getMonthlySalary(), creditRequest.getMonthlySalary());
        assertEquals(creditRequestDto.getPermanentEmployment(), creditRequest.getPermanentEmployment());
        assertEquals(creditRequestDto.getEmploymentPeriod(), creditRequest.getEmploymentPeriod());
        assertEquals(creditRequestDto.getMaturity(), creditRequest.getMaturity());
        assertEquals(creditRequestDto.getBranch(), creditRequest.getBranch());
        assertEquals(creditRequestDto.getMobileNumber(), creditRequest.getMobileNumber());
        assertEquals(creditRequestDto.getAccountNumber(), creditRequest.getAccountNumber());
        assertEquals(creditRequestDto.getNote(), creditRequest.getNote());
        assertEquals(creditRequestDto.getCurrency(), creditRequest.getCurrency());
        assertEquals(creditRequestDto.getEducationLevel(), creditRequest.getEducationLevel());
        assertEquals(creditRequestDto.getMaritalStatus(), creditRequest.getMaritalStatus());
        assertEquals(creditRequestDto.getHousingStatus(), creditRequest.getHousingStatus());
        assertEquals(creditRequestDto.getOwnCar(), creditRequest.getOwnCar());
        assertEquals(creditRequestDto.getPaymentPeriodMonths(), creditRequest.getPaymentPeriodMonths());
        // Verify other mappings as needed
    }

    @Test
    void testCreditRequestToCreditRequestDto() {
        // Given
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setId(123L);
        creditRequest.setStatus(CreditRequestStatus.PENDING);
        creditRequest.setCreditType(CreditType.GOTOVINSKI);
        creditRequest.setCreditAmount(1000.0);
        creditRequest.setCreditPurpose("Test Purpose");
        creditRequest.setMonthlySalary(20000L);
        creditRequest.setPermanentEmployment(true);
        creditRequest.setEmploymentPeriod(12L);
        creditRequest.setMaturity(24L);
        creditRequest.setBranch("Test Branch");
        creditRequest.setMobileNumber("123456789");
        creditRequest.setAccountNumber("987654321");
        creditRequest.setNote("Test Note");
        creditRequest.setCurrency("USD");
        creditRequest.setEducationLevel("Test Education Level");
        creditRequest.setMaritalStatus("Test Marital Status");
        creditRequest.setHousingStatus("Test Housing Status");
        creditRequest.setOwnCar(true);
        creditRequest.setPaymentPeriodMonths(12L);
        // Set other properties as needed

        // When
        CreditRequestDto creditRequestDto = creditMapper.creditRequestToCreditRequestDto(creditRequest);

        // Then
        assertEquals(creditRequest.getId(), creditRequestDto.getId());
        assertEquals(creditRequest.getStatus(), creditRequestDto.getStatus());
        assertEquals(creditRequest.getCreditType(), creditRequestDto.getCreditType());
        assertEquals(creditRequest.getCreditAmount(), creditRequestDto.getCreditAmount());
        assertEquals(creditRequest.getCreditPurpose(), creditRequestDto.getCreditPurpose());
        assertEquals(creditRequest.getMonthlySalary(), creditRequestDto.getMonthlySalary());
        assertEquals(creditRequest.getPermanentEmployment(), creditRequestDto.getPermanentEmployment());
        assertEquals(creditRequest.getEmploymentPeriod(), creditRequestDto.getEmploymentPeriod());
        assertEquals(creditRequest.getMaturity(), creditRequestDto.getMaturity());
        assertEquals(creditRequest.getBranch(), creditRequestDto.getBranch());
        assertEquals(creditRequest.getMobileNumber(), creditRequestDto.getMobileNumber());
        assertEquals(creditRequest.getAccountNumber(), creditRequestDto.getAccountNumber());
        assertEquals(creditRequest.getNote(), creditRequestDto.getNote());
        assertEquals(creditRequest.getCurrency(), creditRequestDto.getCurrency());
        assertEquals(creditRequest.getEducationLevel(), creditRequestDto.getEducationLevel());
        assertEquals(creditRequest.getMaritalStatus(), creditRequestDto.getMaritalStatus());
        assertEquals(creditRequest.getHousingStatus(), creditRequestDto.getHousingStatus());
        assertEquals(creditRequest.getOwnCar(), creditRequestDto.getOwnCar());
        assertEquals(creditRequest.getPaymentPeriodMonths(), creditRequestDto.getPaymentPeriodMonths());
        // Verify other mappings as needed
    }
}
