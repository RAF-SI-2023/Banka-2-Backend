package rs.edu.raf.BankService.unit.securitiesOwnerships;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.controller.SecuritiesOwnershipsController;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.service.SecuritiesOwnershipService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecuritiesOwnershipsControllerTests {

    @Mock
    private  SecuritiesOwnershipService securitiesOwnershipService;

    @InjectMocks
    private  SecuritiesOwnershipsController securitiesOwnershipsController;

    @Test
    public void testGetAllPubliclyAvailableSecurityOwnershipsFromPrivate() {
        // Arrange
        SecuritiesOwnershipDto dto1 = new SecuritiesOwnershipDto(/* initialize with test data */);
        SecuritiesOwnershipDto dto2 = new SecuritiesOwnershipDto(/* initialize with test data */);
        List<SecuritiesOwnershipDto> expectedDtos = Arrays.asList(dto1, dto2);
        when(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnershipsFromPrivates()).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<SecuritiesOwnershipDto>> responseEntity = securitiesOwnershipsController.getAllPubliclyAvailableSecurityOwnershipsFromPrivate();

        // Assert
        assertEquals(expectedDtos, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


    @Test
    public void testGetAllPubliclyAvailableSecurityOwnershipsFromCompanies() {
        // Arrange
        SecuritiesOwnershipDto dto1 = new SecuritiesOwnershipDto(/* initialize with test data */);
        SecuritiesOwnershipDto dto2 = new SecuritiesOwnershipDto(/* initialize with test data */);
        List<SecuritiesOwnershipDto> expectedDtos = Arrays.asList(dto1, dto2);
        when(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnershipsFromCompanies()).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<SecuritiesOwnershipDto>> responseEntity = securitiesOwnershipsController.getAllPubliclyAvailableSecurityOwnershipsFromCompanies();

        // Assert
        assertEquals(expectedDtos, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    @Test
    public void testUpdatePubliclyAvailableQuantity_Success() {
        // Arrange
        SecuritiesOwnershipDto inputDto = new SecuritiesOwnershipDto(/* initialize with test data */);
        SecuritiesOwnershipDto expectedDto = new SecuritiesOwnershipDto(/* initialize with test data */);
        when(securitiesOwnershipService.updatePubliclyAvailableQuantity(inputDto)).thenReturn(expectedDto);

        // Act
        ResponseEntity<?> responseEntity = securitiesOwnershipsController.updatePubliclyAvailableQuantity(inputDto);

        // Assert
        assertEquals(expectedDto, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    public void testUpdatePubliclyAvailableQuantity_Failure() {
        // Arrange
        SecuritiesOwnershipDto inputDto = new SecuritiesOwnershipDto(/* initialize with test data */);
        String errorMessage = "An error occurred";
        doThrow(new RuntimeException(errorMessage)).when(securitiesOwnershipService).updatePubliclyAvailableQuantity(inputDto);

        // Act
        ResponseEntity<?> responseEntity = securitiesOwnershipsController.updatePubliclyAvailableQuantity(inputDto);

        // Assert
        assertEquals(errorMessage, responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllPubliclyAvailableSecurityOwnerships() {
        // Arrange
        SecuritiesOwnershipDto dto1 = new SecuritiesOwnershipDto(/* initialize with test data */);
        SecuritiesOwnershipDto dto2 = new SecuritiesOwnershipDto(/* initialize with test data */);
        List<SecuritiesOwnershipDto> expectedDtos = Arrays.asList(dto1, dto2);
        when(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnerships()).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<SecuritiesOwnershipDto>> responseEntity = securitiesOwnershipsController.getAllPubliclyAvailableSecurityOwnerships();

        // Assert
        assertEquals(expectedDtos, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetSecurityOwnershipsBySecurity() {
        // Arrange
        String securitySymbol = "AAPL";
        SecuritiesOwnershipDto dto1 = new SecuritiesOwnershipDto(/* initialize with test data */);
        SecuritiesOwnershipDto dto2 = new SecuritiesOwnershipDto(/* initialize with test data */);
        List<SecuritiesOwnershipDto> expectedDtos = Arrays.asList(dto1, dto2);
        when(securitiesOwnershipService.getSecurityOwnershipsBySecurity(securitySymbol)).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<SecuritiesOwnershipDto>> responseEntity = securitiesOwnershipsController.getSecurityOwnershipsBySecurity(securitySymbol);

        // Assert
        assertEquals(expectedDtos, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetSecurityOwnershipsForAccountNumber() {
        // Arrange
        String accountNumber = "1234567890";
        SecuritiesOwnershipDto dto1 = new SecuritiesOwnershipDto(/* initialize with test data */);
        SecuritiesOwnershipDto dto2 = new SecuritiesOwnershipDto(/* initialize with test data */);
        List<SecuritiesOwnershipDto> expectedDtos = Arrays.asList(dto1, dto2);
        when(securitiesOwnershipService.getSecurityOwnershipsForAccountNumber(accountNumber)).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<SecuritiesOwnershipDto>> responseEntity = securitiesOwnershipsController.getSecurityOwnershipsForAccountNumber(accountNumber);

        // Assert
        assertEquals(expectedDtos, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
