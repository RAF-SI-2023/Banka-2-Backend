package rs.edu.raf.StockService.unit;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.*;

import rs.edu.raf.StockService.data.dto.BankStockDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.services.ForexService;
import rs.edu.raf.StockService.services.FuturesContractService;
import rs.edu.raf.StockService.services.OptionService;
import rs.edu.raf.StockService.services.StockService;
import rs.edu.raf.StockService.services.facade.BankStockFacadeImpl;

class BankStockFacadeImplTest {

    @Mock
    private StockService stockService;

    @Mock
    private ForexService forexService;

    @Mock
    private FuturesContractService futuresContractService;

    @Mock
    private OptionService optionService;

    @InjectMocks
    private BankStockFacadeImpl bankStockFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindPriceOfUnit_Forex() {
        // Arrange
        BankStockDto bankStockDto = new BankStockDto();
        bankStockDto.setListingType("FOREX");
        bankStockDto.setListingId(1L);

        Forex forex = new Forex();
        forex.setPrice(100.0);

        when(forexService.findById(1L)).thenReturn(forex);

        // Act
        Double price = bankStockFacade.findPriceOfUnit(bankStockDto);

        // Assert
        assertEquals(100.0, price, 0);
        verify(forexService, times(1)).findById(1L);
        verifyNoMoreInteractions(stockService, futuresContractService, optionService);
    }

    @Test
    void testFindPriceOfUnit_Stock() {
        // Arrange
        BankStockDto bankStockDto = new BankStockDto();
        bankStockDto.setListingType("STOCK");
        bankStockDto.setListingId(1L);

        Stock stock = new Stock();
        stock.setPrice(200.0);

        when(stockService.findById(1L)).thenReturn(stock);

        // Act
        Double price = bankStockFacade.findPriceOfUnit(bankStockDto);

        // Assert
        assertEquals(200.0, price);
        verify(stockService, times(1)).findById(1L);
        verifyNoMoreInteractions(forexService, futuresContractService, optionService);
    }

    @Test
    void testFindPriceOfUnit_UnknownType() {
        // Arrange
        BankStockDto bankStockDto = new BankStockDto();
        bankStockDto.setListingType("UNKNOWN");
        bankStockDto.setListingId(1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bankStockFacade.findPriceOfUnit(bankStockDto));
        verifyNoInteractions(stockService, forexService, futuresContractService, optionService);
    }
}
