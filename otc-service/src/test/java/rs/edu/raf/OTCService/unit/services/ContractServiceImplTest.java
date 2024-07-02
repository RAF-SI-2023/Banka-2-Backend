package rs.edu.raf.OTCService.unit.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.entity.Contract;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;
import rs.edu.raf.OTCService.filters.principal.CustomUserPrincipal;
import rs.edu.raf.OTCService.mappers.ContractMapper;
import rs.edu.raf.OTCService.repositories.ContractRepository;
import rs.edu.raf.OTCService.service.BankService;
import rs.edu.raf.OTCService.service.impl.ContractServiceImpl;
import rs.edu.raf.OTCService.util.SpringSecurityUtil;

@ExtendWith(MockitoExtension.class)
public class ContractServiceImplTest {
    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractMapper mapper;

    @Mock
    private BankService bankService;

    @InjectMocks
    private ContractServiceImpl contractService;

    // Get ContractDto for testing, it uses same args as Test Contract below
    ContractDto getTestContractDto() {
        // some args don't matter as they will be set in `createContract()`
        return new ContractDto(
                1L, false,
                false, "comment",
                1L, 1L,
                "12345", "description",
                "ticker", 1,
                100.0, ContractStatus.WAITING,
                1L, 2L,
                "buyer@gmail.com", "seller@gmail.com",
                ContractType.PRIVATE_CONTRACT);
    }

    // Get Contract for testing, it uses same args as Test ContractDto above
    Contract getTestContract() {
        return new Contract(1L, false,
                false, "comment",
                1L, 1L,
                "12345", "description",
                "ticker", 1,
                100.0, ContractStatus.WAITING,
                1L, 2L,
                "buyer@gmail.com", "seller@gmail.com",
                ContractType.PRIVATE_CONTRACT);
    }

    @Test
    public void testGetAllWaitingContracts() {
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext
                .setAuthentication(new TestingAuthenticationToken(new CustomUserPrincipal(1l,
                        "seller@gmail.com"), null, "test"));
        SecurityContextHolder.setContext(securityContext);
        List<Contract> contracts = List.of(getTestContract());
        List<ContractDto> expected = List.of(getTestContractDto());

        when(contractRepository.getAllWaitingContracts()).thenReturn(contracts);

        List<ContractDto> actual = contractService.getAllWaitingContracts();

        assertEquals(expected.size(), actual.size());
        securityContext.setAuthentication(null);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetAllWaitingContracts_isUser() {
        List<Contract> contracts = List.of(getTestContract());
        List<ContractDto> expectedDtos = List.of(getTestContractDto());

        try (MockedStatic<SpringSecurityUtil> mockedStatic = mockStatic(SpringSecurityUtil.class)) {
            mockedStatic.when(SpringSecurityUtil::isUser).thenReturn(true);
            mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn(getTestContract().getSellersEmail());

            when(contractRepository.getAllWaitingContracts()).thenReturn(contracts);
            when(mapper.contractToDto(any(Contract.class))).thenReturn(expectedDtos.get(0));

            List<ContractDto> actualDtos = contractService.getAllWaitingContracts();

            assertEquals(1, actualDtos.size());
            assertEquals(expectedDtos.get(0), actualDtos.get(0));

            verify(contractRepository, times(1)).getAllWaitingContracts();
            verify(mapper, times(1)).contractToDto(any(Contract.class));
        }
    }

    @Test
    public void testGetAllApprovedContracts_isUser() {
        List<Contract> contracts = List.of(getTestContract());
        List<ContractDto> expectedDtos = List.of(getTestContractDto());

        try (MockedStatic<SpringSecurityUtil> mockedStatic = mockStatic(SpringSecurityUtil.class)) {
            mockedStatic.when(SpringSecurityUtil::isUser).thenReturn(true);
            mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn(getTestContractDto().getSellersEmail());

            when(contractRepository.getAllApprovedContracts()).thenReturn(contracts);
            when(mapper.contractToDto(any(Contract.class))).thenReturn(expectedDtos.get(0));

            List<ContractDto> actualDtos = contractService.getAllApprovedContracts();

            assertEquals(1, actualDtos.size());
            assertEquals(expectedDtos.get(0), actualDtos.get(0));

            verify(contractRepository, times(1)).getAllApprovedContracts();
            verify(mapper, times(1)).contractToDto(any(Contract.class));
        }
    }

    @Test
    public void testGetAllRejectedContracts_isUser() {
        List<Contract> contracts = List.of(getTestContract());
        List<ContractDto> expectedDtos = List.of(getTestContractDto());

        try (MockedStatic<SpringSecurityUtil> mockedStatic = mockStatic(SpringSecurityUtil.class)) {
            mockedStatic.when(SpringSecurityUtil::isUser).thenReturn(true);
            mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn(getTestContractDto().getSellersEmail());

            when(contractRepository.getAllRejectedContracts()).thenReturn(contracts);
            when(mapper.contractToDto(any(Contract.class))).thenReturn(expectedDtos.get(0));

            List<ContractDto> actualDtos = contractService.getAllRejectedContracts();

            assertEquals(1, actualDtos.size());
            assertEquals(expectedDtos.get(0), actualDtos.get(0));

            verify(contractRepository, times(1)).getAllRejectedContracts();
            verify(mapper, times(1)).contractToDto(any(Contract.class));
        }
    }

    @Test
    public void testGetAllContracts_isUser() {
        List<Contract> contracts = List.of(getTestContract());
        List<ContractDto> expectedDtos = List.of(getTestContractDto());

        try (MockedStatic<SpringSecurityUtil> mockedStatic = mockStatic(SpringSecurityUtil.class)) {
            mockedStatic.when(SpringSecurityUtil::isUser).thenReturn(true);
            mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn(getTestContractDto().getSellersEmail());

            when(contractRepository.findAll()).thenReturn(contracts);
            when(mapper.contractToDto(any(Contract.class))).thenReturn(expectedDtos.get(0));

            List<ContractDto> actualDtos = contractService.getAllContracts();

            assertEquals(1, actualDtos.size());
            assertEquals(expectedDtos.get(0), actualDtos.get(0));

            verify(contractRepository, times(1)).findAll();
            verify(mapper, times(1)).contractToDto(any(Contract.class));
        }
    }

    @Test
    public void testGetAllApprovedContracts() {
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext
                .setAuthentication(new TestingAuthenticationToken(new CustomUserPrincipal(1l,
                        "seller@gmail.com"), null, "test"));
        SecurityContextHolder.setContext(securityContext);

        List<Contract> contracts = List.of(getTestContract());
        List<ContractDto> expected = List.of(getTestContractDto());

        when(contractRepository.getAllApprovedContracts()).thenReturn(contracts);

        List<ContractDto> actual = contractService.getAllApprovedContracts();

        assertEquals(expected.size(), actual.size());
        securityContext.setAuthentication(null);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    public void testGetAllRejectedContracts() {
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext
                .setAuthentication(new TestingAuthenticationToken(new CustomUserPrincipal(1l,
                        "seller@gmail.com"), null, "test"));
        SecurityContextHolder.setContext(securityContext);

        List<Contract> contracts = List.of(getTestContract());
        List<ContractDto> expected = List.of(getTestContractDto());

        when(contractRepository.getAllRejectedContracts()).thenReturn(contracts);

        List<ContractDto> actual = contractService.getAllRejectedContracts();

        assertEquals(expected.size(), actual.size());
        securityContext.setAuthentication(null);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    public void testGetAllContracts() {
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext
                .setAuthentication(new TestingAuthenticationToken(new CustomUserPrincipal(1l,
                        "seller@gmail.com"), null, "test"));
        SecurityContextHolder.setContext(securityContext);

        List<Contract> contracts = List.of(getTestContract());
        List<ContractDto> expected = List.of(getTestContractDto());

        when(contractRepository.findAll()).thenReturn(contracts);

        List<ContractDto> actual = contractService.getAllContracts();

        assertEquals(expected.size(), actual.size());
        securityContext.setAuthentication(null);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createContract_Success() {
        // create with dto, save and return new dto
        ContractDto contractDto = getTestContractDto();

        Contract contract = getTestContract();

        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        when(mapper.contractToDto(contract)).thenReturn(contractDto);

        ContractDto createdContractDto = contractService.createContract(contractDto);

        verify(mapper, times(1)).contractToDto(contract);
        // don't verify save, contract is edited by create function
        // tested in integration/e2e

        assertEquals(contractDto, createdContractDto);
    }

    @Test
    void getContractById_Success() {
        ContractDto contractDto = getTestContractDto();
        Contract contract = getTestContract();
        long testId = 1L;

        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
        when(mapper.contractToDto(contract)).thenReturn(contractDto);

        ContractDto createdContractDto = contractService.getContractById(testId);

        verify(mapper, times(1)).contractToDto(contract);
        verify(contractRepository, times(1)).findById(testId);

        assertEquals(contractDto, createdContractDto);

    }

    // id not found
    @Test
    void getContractById_Exception() {
        long testId = 1L;

        when(contractRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> {
                    contractService.getContractById(testId);
                });

        verify(contractRepository, times(1)).findById(testId);
    }

    @Test
    void bankApproveContractById_Success() {
        ContractDto contractDto = getTestContractDto();
        contractDto.setBankConfirmation(true);
        contractDto.setSellerConfirmation(true);
        contractDto.setContractStatus(ContractStatus.APPROVED);
        // ignore date time, can't check if equal

        Contract contract = getTestContract();
        contract.setBankConfirmation(false);
        contract.setSellerConfirmation(true);

        long testId = 1L;

        // mock what contract is returned
        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        when(bankService.createTransaction(any(ContractDto.class))).thenReturn(true);
        // contractDto has edited properties and will be same as createdContractDto
        when(mapper.contractToDto(contract)).thenReturn(contractDto);

        ContractDto createdContractDto = contractService.bankApproveContractById(testId);

        verify(contractRepository, times(1)).findById(testId);
        verify(mapper, times(2)).contractToDto(any(Contract.class));
        verify(contractRepository, times(1)).save(any(Contract.class));

        assertNotNull(createdContractDto);
        assertEquals(contractDto.getId(), createdContractDto.getId());
        assertEquals(contractDto.getContractStatus(), createdContractDto.getContractStatus());
        assertEquals(contractDto.getSellerConfirmation(), createdContractDto.getSellerConfirmation());
        assertEquals(contract.getDescription(), createdContractDto.getDescription());
    }

    // id not found and id already confirmed by bank
    @Test
    void bankApproveContractById_Exception() {
        Contract contract = getTestContract();
        contract.setBankConfirmation(true);

        long badId = 1L;
        long testId = 2L;

        // mock what contract is returned
        when(contractRepository.findById(badId)).thenReturn(Optional.empty());
        when(contractRepository.findById(testId)).thenReturn(Optional.of(contract));

        // not found
        assertThrows(RuntimeException.class, () -> {
            contractService.bankApproveContractById(badId);
        });

        // `BankConfirmation` is True
        assertThrows(RuntimeException.class, () -> {
            contractService.bankApproveContractById(testId);
        });
    }

    @Test
    void sellerApproveContractById_Success() {
        ContractDto contractDto = getTestContractDto();
        contractDto.setSellerConfirmation(true);
        contractDto.setBankConfirmation(true);
        contractDto.setContractStatus(ContractStatus.APPROVED);
        // ignore date time, can't check if equal

        String testEmail = "testEmail@gmail.com";

        Contract contract = getTestContract();
        contract.setBankConfirmation(true);
        contract.setSellerConfirmation(false);
        contract.setSellersEmail(testEmail);

        long testId = 1L;

        // mock what contract is returned
        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        when(bankService.createTransaction(any(ContractDto.class))).thenReturn(true);
        // contractDto has edited properties and will be same as createdContractDto
        when(mapper.contractToDto(contract)).thenReturn(contractDto);

        try (MockedStatic<SpringSecurityUtil> mockedStatic = mockStatic(SpringSecurityUtil.class)) {
            mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn(testEmail);

            ContractDto createdContractDto = contractService.sellerApproveContractById(testId);

            verify(contractRepository, times(1)).findById(testId);
            verify(mapper, times(2)).contractToDto(any(Contract.class));
            verify(contractRepository, times(1)).save(any(Contract.class));

            assertNotNull(createdContractDto);
            assertEquals(contractDto.getId(), createdContractDto.getId());
            assertEquals(contractDto.getContractStatus(), createdContractDto.getContractStatus());
            assertEquals(contractDto.getSellerConfirmation(), createdContractDto.getSellerConfirmation());
            assertEquals(contract.getDescription(), createdContractDto.getDescription());
        }
    }

    // id not found and id already confirmed and wrong email
    @Test
    void sellerApproveContractById_Exception() {
        String testEmail = "testEmail@gmail.com";
        String wrongEmail = "badEmail@gmail.com";

        Contract contractConfirmed = getTestContract();
        contractConfirmed.setSellerConfirmation(false);

        Contract contractNotTheSeller = getTestContract();
        contractNotTheSeller.setBankConfirmation(true);
        contractNotTheSeller.setSellerConfirmation(false);
        contractNotTheSeller.setSellersEmail(wrongEmail);

        long badId = 1L;
        long testIdConfirmed = 2L;
        long testIdNotTheSeller = 3L;

        // mock what contract is returned
        when(contractRepository.findById(badId)).thenReturn(Optional.empty());
        when(contractRepository.findById(testIdConfirmed)).thenReturn(Optional.of(contractConfirmed));
        when(contractRepository.findById(testIdNotTheSeller)).thenReturn(Optional.of(contractNotTheSeller));

        // not found
        assertThrows(RuntimeException.class, () -> {
            contractService.sellerApproveContractById(badId);
        });

        // already confirmed
        assertThrows(RuntimeException.class, () -> {
            contractService.sellerApproveContractById(testIdConfirmed);
        });

        // wrong user email
        assertThrows(RuntimeException.class, () -> {
            try (MockedStatic<SpringSecurityUtil> mockedStatic = mockStatic(SpringSecurityUtil.class)) {
                mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn(testEmail);

                contractService.sellerApproveContractById(testIdNotTheSeller);
            }
        });
    }

    @Test
    void bankDenyContractById_Success() {
        String comment = "testComment";
        ContractDto contractDto = getTestContractDto();
        contractDto.setBankConfirmation(false);
        contractDto.setContractStatus(ContractStatus.REJECTED);
        contractDto.setComment(comment);
        // ignore date time, can't check if equal

        Contract contract = getTestContract();
        contract.setBankConfirmation(true); // not denied

        long testId = 1L;

        // mock what contract is returned
        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        // contractDto has edited properties and will be same as createdContractDto
        when(mapper.contractToDto(contract)).thenReturn(contractDto);

        ContractDto createdContractDto = contractService.bankDenyContractById(testId, comment);

        verify(contractRepository, times(1)).findById(testId);
        verify(mapper, times(1)).contractToDto(any(Contract.class));
        verify(contractRepository, times(1)).save(any(Contract.class));

        assertNotNull(createdContractDto);
        assertEquals(contractDto.getId(), createdContractDto.getId());
        assertEquals(contractDto.getContractStatus(), createdContractDto.getContractStatus());
        assertEquals(contract.getComment(), createdContractDto.getComment());
    }

    // id not found and already denied
    @Test
    void bankDenyContractById_Exception() {
        String comment = "testComment";
        Contract contract = getTestContract();
        contract.setBankConfirmation(false); // not denied

        long badId = 1L;
        long testId = 2L;

        // mock what contract is returned
        when(contractRepository.findById(badId)).thenReturn(Optional.empty());
        when(contractRepository.findById(testId)).thenReturn(Optional.of(contract));

        // not found
        assertThrows(RuntimeException.class, () -> {
            contractService.bankDenyContractById(badId, comment);
        });

        // already denied
        assertThrows(RuntimeException.class, () -> {
            contractService.bankDenyContractById(testId, comment);
        });
    }

    @Test
    void sellerDenyContractById_Success() {
        String comment = "testComment";
        ContractDto contractDto = getTestContractDto();
        contractDto.setSellerConfirmation(false);
        contractDto.setContractStatus(ContractStatus.REJECTED);
        contractDto.setComment(comment);
        // ignore date time, can't check if equal

        String testEmail = "testEmail@gmail.com";
        Contract contract = getTestContract();
        contract.setSellerConfirmation(true); // not denied
        contract.setSellersEmail(testEmail);

        long testId = 1L;

        // mock what contract is returned
        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        // contractDto has edited properties and will be same as createdContractDto
        when(mapper.contractToDto(contract)).thenReturn(contractDto);

        try (MockedStatic<SpringSecurityUtil> mockedStatic = mockStatic(SpringSecurityUtil.class)) {
            mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn(testEmail);

            ContractDto createdContractDto = contractService.sellerDenyContractById(testId, comment);

            verify(contractRepository, times(1)).findById(testId);
            verify(mapper, times(1)).contractToDto(any(Contract.class));
            verify(contractRepository, times(1)).save(any(Contract.class));

            assertNotNull(createdContractDto);
            assertEquals(contractDto.getId(), createdContractDto.getId());
            assertEquals(contractDto.getContractStatus(), createdContractDto.getContractStatus());
            assertEquals(contract.getComment(), createdContractDto.getComment());
        }
    }

    // id not found and already denied and wrong email
    @Test
    void sellerDenyContractById_Exception() {
        String comment = "testComment";
        String testEmail = "testEmail@gmail.com";
        String wrongEmail = "badEmail@gmail.com";

        Contract contractConfirmed = getTestContract();
        contractConfirmed.setSellerConfirmation(true);

        Contract contractNotTheSeller = getTestContract();
        contractNotTheSeller.setSellerConfirmation(false);
        contractNotTheSeller.setSellersEmail(wrongEmail);

        long badId = 1L;
        long testIdConfirmed = 2L;
        long testIdNotTheSeller = 3L;

        // mock what contract is returned
        when(contractRepository.findById(badId)).thenReturn(Optional.empty());
        when(contractRepository.findById(testIdConfirmed)).thenReturn(Optional.of(contractConfirmed));
        when(contractRepository.findById(testIdNotTheSeller)).thenReturn(Optional.of(contractNotTheSeller));

        // not found
        assertThrows(RuntimeException.class, () -> {
            contractService.sellerDenyContractById(badId, comment);
        });

        // already denied
        assertThrows(RuntimeException.class, () -> {
            contractService.sellerDenyContractById(testIdConfirmed, comment);
        });

        // wrong user email
        assertThrows(RuntimeException.class, () -> {
            try (MockedStatic<SpringSecurityUtil> mockedStatic = mockStatic(SpringSecurityUtil.class)) {
                mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn(testEmail);

                contractService.sellerDenyContractById(testIdNotTheSeller, comment);
            }
        });
    }
}
