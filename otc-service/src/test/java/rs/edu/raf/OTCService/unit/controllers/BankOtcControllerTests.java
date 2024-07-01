package rs.edu.raf.OTCService.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rabbitmq.client.RpcClient.Response;

import io.cucumber.java.en.When;
import rs.edu.raf.OTCService.controllers.BankOtcController;
import rs.edu.raf.OTCService.data.dto.testing.FrontendOfferDto;
import rs.edu.raf.OTCService.data.dto.testing.MyStockDto;
import rs.edu.raf.OTCService.data.dto.testing.OfferDto;
import rs.edu.raf.OTCService.data.entity.listing.BankOTCStock;
import rs.edu.raf.OTCService.data.entity.offer.MyOffer;
import rs.edu.raf.OTCService.data.entity.offer.Offer;
import rs.edu.raf.OTCService.service.test.BankOtcService;

public class BankOtcControllerTests {

    @Mock
    private BankOtcService bankOtcService;

    @InjectMocks
    private BankOtcController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMyStocks() {
        List<MyStockDto> expected = new ArrayList<>();
        expected.add(new MyStockDto());
        expected.add(new MyStockDto());

        when(bankOtcService.findAllStocks()).thenReturn(expected);

        ResponseEntity<List<MyStockDto>> res = controller.getMyStocks();

        assertEquals(expected.size(), res.getBody().size());
    }

    @Test
    public void testReceiveOfferBank4() {
        OfferDto dto = new OfferDto();
        dto.setAmount(20);
        dto.setIdBank(4l);
        dto.setPrice(25.0);
        dto.setTicker("Peki");

        Offer expected = new Offer();

        when(bankOtcService.receiveOffer(dto)).thenReturn(expected);

        ResponseEntity<Offer> res = controller.receiveOfferBank4(dto);

        assertEquals(expected, res.getBody());
    }

    @Test
    public void testOfferAcceptedBank4() {
        Long offerId = 1l;
        when(bankOtcService.offerAccepted(offerId)).thenReturn(true);

        ResponseEntity<MyOffer> res = controller.offerAcceptedBank4(offerId);

        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void testOfferAcceptedBank4_Fail() {
        Long offerId = 1l;
        when(bankOtcService.offerAccepted(offerId)).thenReturn(false);

        ResponseEntity<MyOffer> res = controller.offerAcceptedBank4(offerId);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void offerDeclinedBank4() {
        Long offerId = 1l;
        when(bankOtcService.offerDeclined(offerId)).thenReturn(true);

        ResponseEntity<MyOffer> res = controller.offerDeclinedBank4(offerId);

        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void testGetBankStocks() {
        List<BankOTCStock> expected = new ArrayList<>();
        expected.add(new BankOTCStock());
        expected.add(new BankOTCStock());

        when(bankOtcService.getAllStocksForBanks()).thenReturn(expected);

        ResponseEntity<List<BankOTCStock>> res = controller.getBanksStocks();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(expected.size(), res.getBody().size());
    }

    @Test
    public void testGetOffers() {
        List<Offer> expected = new ArrayList<>();
        expected.add(new Offer());
        expected.add(new Offer());

        when(bankOtcService.findAllOffers()).thenReturn(expected);

        ResponseEntity<List<Offer>> res = controller.getOffers();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(expected.size(), res.getBody().size());
    }

    @Test
    public void testGetMyOffers() {
        List<MyOffer> expected = new ArrayList<>();
        expected.add(new MyOffer());
        expected.add(new MyOffer());

        when(bankOtcService.getMyOffers()).thenReturn(expected);

        ResponseEntity<List<MyOffer>> res = controller.getMyOffers();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(expected.size(), res.getBody().size());
    }

    @Test
    public void testRefreshOtc() {
        ResponseEntity<Offer> res = controller.refreshOTC();
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void testMakeOffer() {
        FrontendOfferDto dto = new FrontendOfferDto();
        when(bankOtcService.makeOffer(dto)).thenReturn(true);
        ResponseEntity<MyOffer> res = controller.makeOffer(dto);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void testAcceptOffer() {
        Long offerId = 1l;
        when(bankOtcService.acceptOffer(offerId)).thenReturn(true);
        ResponseEntity<Offer> res = controller.acceptOffer(offerId);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void testDeclineOffer() {
        Long offerId = 1l;
        when(bankOtcService.declineOffer(offerId)).thenReturn(true);
        ResponseEntity<Offer> res = controller.declineOffer(offerId);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void testDeleteMyOffer() {
        Long offerId = 1l;
        when(bankOtcService.deleteMyOffer(offerId)).thenReturn(true);
        ResponseEntity<?> res = controller.deleteMyOffer(offerId);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void testDeleteOffer() {
        Long offerId = 1l;
        when(bankOtcService.deleteOffer(offerId)).thenReturn(true);
        ResponseEntity<?> res = controller.deleteOffer(offerId);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }
}
