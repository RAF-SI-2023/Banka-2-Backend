package rs.edu.raf.StockService.unit;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.mapper.FuturesContractMapper;
import rs.edu.raf.StockService.repositories.FuturesContractRepository;
import rs.edu.raf.StockService.services.impl.FuturesContractImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FutureServiceImplTests {
    @InjectMocks
    FuturesContractImpl futuresContractServiceImpl;

    @Mock
    FuturesContractRepository futuresContractRepository;
    @Mock
    FuturesContractMapper mapper;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testFindAll() {
        // Mock behavior
        List<FuturesContract> contracts = new ArrayList<>();
        FuturesContract contract=new FuturesContract(/* initialize contract here */);
        contracts.add(contract);
        List<FuturesContractDto> contractsDto = new ArrayList<>();
        FuturesContractDto contractDto=new FuturesContractDto(/* initialize contract here */);
        contractsDto.add(contractDto);

        when(futuresContractRepository.findAll()).thenReturn(contracts);
        when(mapper.futuresContractToFuturesContractDto(contract)).thenReturn(contractDto);
        // Test
        List<FuturesContractDto> result = futuresContractServiceImpl.findAll();

        // Verify
        assertEquals(contractsDto, result);
    }

    @Test
    public void testSave() {
        // Mock behavior
        FuturesContract contract = new FuturesContract(/* initialize contract here */);
        when(futuresContractRepository.save(eq(contract))).thenReturn(contract);

        // Test
        FuturesContract savedContract = futuresContractServiceImpl.save(contract);

        // Verify
        assertEquals(contract, savedContract);
        verify(futuresContractRepository).save(eq(contract));
    }

    @Test
    public void testFindById_ExistingId() {
        // Mock behavior
        Long id = 1L;
        FuturesContract contract = new FuturesContract(/* initialize contract here */);
        FuturesContractDto dto=new FuturesContractDto();
        when(futuresContractRepository.findById(eq(id))).thenReturn(Optional.of(contract));
        when(mapper.futuresContractToFuturesContractDto(contract)).thenReturn(dto);
        // Test
        FuturesContractDto foundContract = futuresContractServiceImpl.findById(id);

        // Verify
        assertEquals(dto, foundContract);
    }

    @Test
    public void testFindById_NonExistingId() {
        // Mock behavior
        Long id = 1L;
        when(futuresContractRepository.findById(eq(id))).thenThrow(new RuntimeException("Futures contract with " + id + " not found"));

        assertThrows(RuntimeException.class, () -> futuresContractServiceImpl.findById(id));

        // Test


        // Expect RuntimeException
    }
}


