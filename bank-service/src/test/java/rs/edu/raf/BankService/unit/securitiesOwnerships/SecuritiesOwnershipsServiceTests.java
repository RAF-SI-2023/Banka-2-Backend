package rs.edu.raf.BankService.unit.securitiesOwnerships;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.mapper.SecuritiesOwnershipMapper;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.impl.SecuritiesOwnershipServiceImpl;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecuritiesOwnershipsServiceTests {

    @Mock
    public SecuritiesOwnershipRepository securitiesOwnershipRepository;
    @Mock
    public SecuritiesOwnershipMapper mapper;
    @InjectMocks
    private SecuritiesOwnershipServiceImpl securitiesOwnershipService;

    @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
    private String myEmail1;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSecurityOwnershipsForAccountNumber() {
        // Arrange
        String accountNumber = "1234567890";
        SecuritiesOwnership ownership1 = new SecuritiesOwnership(/* initialize with test data */);
        ownership1.setAccountNumber(accountNumber);
        ownership1.setQuantity(10);
        ownership1.setSecuritiesSymbol("AAPL");
        SecuritiesOwnership ownership2 = new SecuritiesOwnership(/* initialize with test data */);
        ownership2.setAccountNumber(accountNumber);
        ownership2.setQuantity(20);
        ownership2.setSecuritiesSymbol("GOOGL");
        List<SecuritiesOwnership> ownershipList = Arrays.asList(ownership1, ownership2);
        SecuritiesOwnershipDto dto1 = new SecuritiesOwnershipDto(/* initialize with test data */);
        SecuritiesOwnershipDto dto2 = new SecuritiesOwnershipDto(/* initialize with test data */);
        List<SecuritiesOwnershipDto> expectedDtos = Arrays.asList(dto1, dto2);

        when(securitiesOwnershipRepository.findAllByAccountNumber(accountNumber)).thenReturn(ownershipList);
        when(mapper.toDto(ownership1)).thenReturn(dto1);
        when(mapper.toDto(ownership2)).thenReturn(dto2);

        // Act
        List<SecuritiesOwnershipDto> actualDtos = securitiesOwnershipService.getSecurityOwnershipsForAccountNumber(accountNumber);

        // Assert
        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    public void testGetSecurityOwnershipsBySecurity() {
        String securitySymbol = "AAPL";
        List<SecuritiesOwnership> ownerships = List.of(
                new SecuritiesOwnership(/* initialize with test data */),
                new SecuritiesOwnership(/* initialize with test data */)
        );

        List<SecuritiesOwnershipDto> dtos = List.of(
                new SecuritiesOwnershipDto(/* initialize with test data */),
                new SecuritiesOwnershipDto(/* initialize with test data */)
        );

        when(securitiesOwnershipRepository.findAllBySecuritiesSymbol(securitySymbol)).thenReturn(ownerships);
        when(mapper.toDto(ownerships.get(0))).thenReturn(dtos.get(0));
        when(mapper.toDto(ownerships.get(1))).thenReturn(dtos.get(1));

        List<SecuritiesOwnershipDto> result = securitiesOwnershipService.getSecurityOwnershipsBySecurity(securitySymbol);

        assertEquals(dtos, result);
    }

    @Test
    public void testGetAllPubliclyAvailableSecurityOwnerships() {
        List<SecuritiesOwnership> ownerships = List.of(
                new SecuritiesOwnership(/* initialize with test data */),
                new SecuritiesOwnership(/* initialize with test data */)
        );

        List<SecuritiesOwnershipDto> dtos = List.of(
                new SecuritiesOwnershipDto(/* initialize with test data */),
                new SecuritiesOwnershipDto(/* initialize with test data */)
        );

        when(securitiesOwnershipRepository.findSecuritiesOwnershipByQuantityOfPubliclyAvailable()).thenReturn(ownerships);
        when(mapper.toDto(ownerships.get(0))).thenReturn(dtos.get(0));
        when(mapper.toDto(ownerships.get(1))).thenReturn(dtos.get(1));

        List<SecuritiesOwnershipDto> result = securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnerships();

        assertEquals(dtos, result);
    }

    @Test
    public void testGetAllPubliclyAvailableSecurityOwnershipsFromCompanies() {
        List<SecuritiesOwnership> ownerships = List.of(
                new SecuritiesOwnership(/* initialize with test data */),
                new SecuritiesOwnership(/* initialize with test data */)
        );

        List<SecuritiesOwnershipDto> dtos = List.of(
                new SecuritiesOwnershipDto(/* initialize with test data */),
                new SecuritiesOwnershipDto(/* initialize with test data */)
        );

        when(securitiesOwnershipRepository.findSecuritiesOwnershipByQuantityOfPubliclyAvailableAndIsBusiness()).thenReturn(ownerships);
        when(mapper.toDto(ownerships.get(0))).thenReturn(dtos.get(0));
        when(mapper.toDto(ownerships.get(1))).thenReturn(dtos.get(1));

        List<SecuritiesOwnershipDto> result = securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnershipsFromCompanies();

        assertEquals(dtos, result);
    }

    @Test
    public void testGetAllPubliclyAvailableSecurityOwnershipsFromPrivates() {
        List<SecuritiesOwnership> ownerships = List.of(
                new SecuritiesOwnership(/* initialize with test data */),
                new SecuritiesOwnership(/* initialize with test data */)
        );

        List<SecuritiesOwnershipDto> dtos = List.of(
                new SecuritiesOwnershipDto(/* initialize with test data */),
                new SecuritiesOwnershipDto(/* initialize with test data */)
        );

        when(securitiesOwnershipRepository.findSecuritiesOwnershipByQuantityOfPubliclyAvailableAndIsPrivate()).thenReturn(ownerships);
        when(mapper.toDto(ownerships.get(0))).thenReturn(dtos.get(0));
        when(mapper.toDto(ownerships.get(1))).thenReturn(dtos.get(1));

        List<SecuritiesOwnershipDto> result = securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnershipsFromPrivates();

        assertEquals(dtos, result);
    }

    @Test
    public void testUpdatePubliclyAvailableQuantity_WhenInvalidId_ExpectExceptionThrown() {
        // Mock data
        SecuritiesOwnershipDto inputDto = new SecuritiesOwnershipDto(/* Initialize with invalid id */);

        // Mock repository behavior
        when(securitiesOwnershipRepository.findById(inputDto.getId())).thenReturn(Optional.empty());

        // Invoke method and verify exception
        assertThrows(RuntimeException.class, () -> securitiesOwnershipService.updatePubliclyAvailableQuantity(inputDto));

        // Verify repository interaction
        verify(securitiesOwnershipRepository).findById(inputDto.getId());
        verifyNoMoreInteractions(securitiesOwnershipRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    public void testUpdatePubliclyAvailableQuantity_WhenInvalidQuantity_ExpectExceptionThrown() {
        // Mock data
        SecuritiesOwnershipDto inputDto = new SecuritiesOwnershipDto(/* Initialize with invalid quantity */);
        SecuritiesOwnership securitiesOwnership = new SecuritiesOwnership(/* Initialize with valid data */);

        // Mock repository behavior
        when(securitiesOwnershipRepository.findById(inputDto.getId())).thenReturn(Optional.of(securitiesOwnership));

        // Invoke method and verify exception
        assertThrows(RuntimeException.class, () -> securitiesOwnershipService.updatePubliclyAvailableQuantity(inputDto));

        // Verify repository interaction
        verify(securitiesOwnershipRepository).findById(inputDto.getId());
        verifyNoMoreInteractions(securitiesOwnershipRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    public void testUpdatePubliclyAvailableQuantity_WhenValidInput_ExpectDtoReturned() {
        // Mock data
        SecuritiesOwnershipDto inputDto = new SecuritiesOwnershipDto(/* Initialize with valid data */);
        inputDto.setId(1L); // Set a valid ID here
        inputDto.setQuantityOfPubliclyAvailable(10);
        inputDto.setQuantity(50);
        inputDto.setSecuritiesSymbol("AAPL");
        inputDto.setEmail(myEmail1);
        inputDto.setAccountNumber("3334444999999999");
        inputDto.setReservedQuantity(0);
        inputDto.setOwnedByBank(false);

        SecuritiesOwnership securitiesOwnership = new SecuritiesOwnership(/* Initialize with valid data */);
        securitiesOwnership.setId(inputDto.getId()); // Set the ID to match inputDto
        securitiesOwnership.setQuantity(50);
        securitiesOwnership.setOwnedByBank(false);
        securitiesOwnership.setReservedQuantity(0);
        securitiesOwnership.setQuantityOfPubliclyAvailable(10);
        securitiesOwnership.setSecuritiesSymbol("AAPL");
        securitiesOwnership.setEmail(myEmail1);
        securitiesOwnership.setAccountNumber("3334444999999999");

        // Mock repository behavior
        when(securitiesOwnershipRepository.findById(inputDto.getId())).thenReturn(Optional.of(securitiesOwnership));
        when(mapper.toDto(any(SecuritiesOwnership.class))).thenAnswer(invocation -> {
            SecuritiesOwnership arg = invocation.getArgument(0);
            return new SecuritiesOwnershipDto(/* Map arg to Dto */);
        });

        when(securitiesOwnershipRepository.save(securitiesOwnership)).thenReturn(securitiesOwnership);
        // Invoke method
        SecuritiesOwnershipDto resultDto = securitiesOwnershipService.updatePubliclyAvailableQuantity(inputDto);


        // Verify repository interactions
        verify(securitiesOwnershipRepository).findById(inputDto.getId());
        verify(securitiesOwnershipRepository).save(securitiesOwnership);

        // Verify mapper interactions
        verify(mapper).toDto(securitiesOwnership); // Pass securitiesOwnership instead of null

        // Verify result
        assertNotNull(resultDto);
        // Add more assertions based on the specific logic of your method
    }

    @Test
    public void testGetValuesOfSecurities() {
        // Arrange
        String accountNumber = "1234567890";
        SecuritiesOwnership ownership1 = new SecuritiesOwnership(/* initialize with test data */);
        ownership1.setAccountNumber(accountNumber);
        ownership1.setAverageBuyingPrice(100.0);
        ownership1.setQuantity(10);
        ownership1.setListingType(ListingType.STOCK);
        SecuritiesOwnership ownership2 = new SecuritiesOwnership(/* initialize with test data */);
        ownership2.setAccountNumber(accountNumber);
        ownership2.setAverageBuyingPrice(200.0);
        ownership2.setQuantity(20);
        ownership2.setListingType(ListingType.FOREX);
        List<SecuritiesOwnership> ownershipList = Arrays.asList(ownership1, ownership2);

        Map<ListingType, BigDecimal> expectedValues = new HashMap<>();
        expectedValues.put(ListingType.STOCK, BigDecimal.valueOf(1000.0));
        expectedValues.put(ListingType.FOREX, BigDecimal.valueOf(4000.0));
        expectedValues.put(ListingType.OPTION, BigDecimal.ZERO);
        expectedValues.put(ListingType.FUTURE, BigDecimal.ZERO);

        when(securitiesOwnershipRepository.existsByAccountNumber(accountNumber)).thenReturn(true);
        when(securitiesOwnershipRepository.findAllByAccountNumber(accountNumber)).thenReturn(ownershipList);

        // Act
        Map<ListingType, BigDecimal> actualValues = securitiesOwnershipService.getValuesOfSecurities(accountNumber);

        // Assert
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetValuesOfSecurities_WhenAccountNotFound() {
        // Arrange
        String accountNumber = "1234567890";

        when(securitiesOwnershipRepository.existsByAccountNumber(accountNumber)).thenReturn(false);

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> securitiesOwnershipService.getValuesOfSecurities(accountNumber));
    }

}
