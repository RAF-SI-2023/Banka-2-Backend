package rs.edu.raf.StockService.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rs.edu.raf.StockService.data.dto.BankStockDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.services.facade.BankStockFacadeImpl;

public class BankServiceCommunicationControllerTests {
    @Mock
    private BankStockFacadeImpl bankStockFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindPrice() {
        BankStockDto dto1 = new BankStockDto();
        dto1.setListingId(1l);
        dto1.setListingType("FOREX");

        BankStockDto dto2 = new BankStockDto();
        dto2.setListingId(2l);
        dto2.setListingType("STOCK");

        Forex forex = new Forex();
        forex.setPrice(25.0);
        Stock stock = new Stock();
        stock.setPrice(245.1);

        when(bankStockFacade.findPriceOfUnit(dto1)).thenReturn(forex.getPrice());
        when(bankStockFacade.findPriceOfUnit(dto2)).thenReturn(stock.getPrice());

        Double price1 = bankStockFacade.findPriceOfUnit(dto1);
        Double price2 = bankStockFacade.findPriceOfUnit(dto2);

        assertEquals(price1, forex.getPrice());
        assertEquals(price2, stock.getPrice());
    }
}
