package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.data.enums.FuturesContractType;
import rs.edu.raf.StockService.mapper.FuturesContractMapper;
import rs.edu.raf.StockService.repositories.FuturesContractRepository;
import rs.edu.raf.StockService.services.impl.FuturesContractImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuturesContractImplTest {
    @Mock
    private FuturesContractRepository futuresContractRepository;

    @Mock
    private FuturesContractMapper mapper;

    @InjectMocks
    private FuturesContractImpl futuresContractService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<FuturesContract> contracts = new ArrayList<>();

        long settlementDate = System.currentTimeMillis();
        contracts.add(
                new FuturesContract(
                        1L,
                        "sugar",
                        "AMH22",
                        500,
                        "kg",
                        300,
                        settlementDate,
                        14000,
                        FuturesContractType.AGRICULTURE, 200.0, 15.0));

        settlementDate = System.currentTimeMillis();
        contracts.add(
                new FuturesContract(
                        2L,
                        "coal",
                        "AGD23",
                        1000,
                        "t",
                        850,
                        settlementDate,
                        28000,
                        FuturesContractType.ENERGY, 500.0, 56.0));

        when(futuresContractRepository.findAll()).thenReturn(contracts);

        // Act
        List<FuturesContract> result = futuresContractRepository.findAll();

        // Assert
        assertEquals(contracts.size(), result.size());
        verify(futuresContractRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        long settlementDate = System.currentTimeMillis();
        FuturesContract contractToSave = new FuturesContract(
                1L,
                "sugar",
                "AMH22",
                500,
                "kg",
                300,
                settlementDate,
                14000,
                FuturesContractType.AGRICULTURE,
                20.0,
                20.0);
        when(futuresContractRepository.save(contractToSave)).thenReturn(contractToSave);

        // Act
        FuturesContract savedContract = futuresContractService.save(contractToSave);

        // Assert
        assertEquals(contractToSave, savedContract);
        verify(futuresContractRepository, times(1)).save(contractToSave);
    }

    @Test
    public void testFindById() {
        FuturesContract contract = new FuturesContract(
                1L,
                "sugar",
                "AMH22",
                500,
                "kg",
                300,
                System.currentTimeMillis(),
                14000,
                FuturesContractType.AGRICULTURE,
                20.0,
                20.0);

        when(futuresContractRepository.findById(1l)).thenReturn(Optional.of(contract));
        when(mapper.futuresContractToFuturesContractDto(contract)).thenReturn(new FuturesContractDto(
                contract.getId(),
                contract.getName(),
                contract.getCode(),
                contract.getContractSize(),
                contract.getContractUnit(),
                contract.getOpenInterest(),
                contract.getSettlementDate(),
                contract.getMaintenanceMargin(),
                contract.getType(),
                contract.getFuturesContractPrice(),
                contract.getRawMaterialPrice()));

        FuturesContractDto actual = futuresContractService.findById(1l);

        assertEquals(contract.getId(), actual.getId());
    }

    @Test
    public void testFindByName() {
        FuturesContract contract = new FuturesContract(
                1L,
                "sugar",
                "AMH22",
                500,
                "kg",
                300,
                System.currentTimeMillis(),
                14000,
                FuturesContractType.AGRICULTURE,
                20.0,
                20.0);

        when(futuresContractRepository.findByName("sugar")).thenReturn(Optional.of(contract));
        when(mapper.futuresContractToFuturesContractDto(contract)).thenReturn(new FuturesContractDto(
                contract.getId(),
                contract.getName(),
                contract.getCode(),
                contract.getContractSize(),
                contract.getContractUnit(),
                contract.getOpenInterest(),
                contract.getSettlementDate(),
                contract.getMaintenanceMargin(),
                contract.getType(),
                contract.getFuturesContractPrice(),
                contract.getRawMaterialPrice()));

        FuturesContractDto actual = futuresContractService.findByName("sugar");

        assertEquals(contract.getId(), actual.getId());
        assertEquals(contract.getName(), actual.getName());
    }
}