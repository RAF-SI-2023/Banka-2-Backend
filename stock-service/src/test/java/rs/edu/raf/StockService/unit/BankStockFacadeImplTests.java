package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.dto.BankStockDto;
import rs.edu.raf.StockService.data.dto.StockDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.services.ForexService;
import rs.edu.raf.StockService.services.FuturesContractService;
import rs.edu.raf.StockService.services.OptionService;
import rs.edu.raf.StockService.services.StockService;
import rs.edu.raf.StockService.services.facade.BankStockFacadeImpl;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        bankStockDto.setListingName("USD/CAD");

        Forex forex = new Forex();
        forex.setPrice(100.0);

        when(forexService.findBySymbol("USD/CAD")).thenReturn(forex);

        // Act
        Double price = bankStockFacade.findPriceOfUnit(bankStockDto);

        // Assert
        assertEquals(100.0, price, 0);
        verify(forexService, times(1)).findBySymbol("USD/CAD");
        verifyNoMoreInteractions(stockService, futuresContractService, optionService);
    }

    @Test
    void testFindPriceOfUnit_Stock() {
        // Arrange
        BankStockDto bankStockDto = new BankStockDto();
        bankStockDto.setListingType("STOCK");
        bankStockDto.setListingName("GOOGL");

        StockDto stock = new StockDto();
        stock.setPrice(200.0);

        when(stockService.findBySymbol("GOOGL")).thenReturn(stock);

        // Act
        Double price = bankStockFacade.findPriceOfUnit(bankStockDto);

        // Assert
        assertEquals(200.0, price);
        verify(stockService, times(1)).findBySymbol("GOOGL");
        verifyNoMoreInteractions(forexService, futuresContractService, optionService);
    }

    @Test
    void testFindPriceOfUnit_UnknownType() {
        // Arrange
        BankStockDto bankStockDto = new BankStockDto();
        bankStockDto.setListingType("UNKNOWN");
        bankStockDto.setListingName("UNKOWN");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bankStockFacade.findPriceOfUnit(bankStockDto));
        verifyNoInteractions(stockService, forexService, futuresContractService, optionService);
    }
}
