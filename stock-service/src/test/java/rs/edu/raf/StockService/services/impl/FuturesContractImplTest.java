package rs.edu.raf.StockService.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.data.enums.FuturesContractType;
import rs.edu.raf.StockService.repositories.FuturesContractRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuturesContractImplTest {
    @Mock
    private FuturesContractRepository futuresContractRepository;

    @InjectMocks
    private FuturesContractImpl futuresContractService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
/*
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
                        FuturesContractType.AGRICULTURE
                )
        );

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
                        FuturesContractType.ENERGY
                )
        );

        when(futuresContractRepository.findAll()).thenReturn(contracts);

        // Act
        List<FuturesContractDto> result = futuresContractService.findAll();

        // Assert
        assertEquals(contracts.size(), result.size());
        verify(futuresContractRepository, times(1)).findAll();
    }*/

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
                20.0
        );
        when(futuresContractRepository.save(contractToSave)).thenReturn(contractToSave);

        // Act
        FuturesContract savedContract = futuresContractService.save(contractToSave);

        // Assert
        assertEquals(contractToSave, savedContract);
        verify(futuresContractRepository, times(1)).save(contractToSave);
    }
}