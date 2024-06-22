package rs.edu.raf.OTCService.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.OTCService.data.dto.testing.FrontendOfferDto;
import rs.edu.raf.OTCService.data.dto.testing.MyStockDto;
import rs.edu.raf.OTCService.data.dto.testing.OfferDto;
import rs.edu.raf.OTCService.data.entity.listing.BankOTCStock;
import rs.edu.raf.OTCService.data.entity.offer.MyOffer;
import rs.edu.raf.OTCService.data.entity.offer.Offer;
import rs.edu.raf.OTCService.service.test.BankOtcService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/otcTrade")
public class BankOtcController {
    private final BankOtcService bankOtcService;

    @GetMapping("/getOurStocks")
    @Operation(description = "dohvata stockove nase banke")
    public ResponseEntity<List<MyStockDto>> getMyStocks(){
        return ResponseEntity.ok(this.bankOtcService.findAllStocks());
    }

    @PostMapping("/sendOffer/bank3")
    @Operation(description = "primamo ponude od banke 3")
    public ResponseEntity<Offer> receiveOfferBank4(@RequestBody OfferDto dto){
        return ResponseEntity.ok(bankOtcService.receiveOffer(dto));
    }

    @PostMapping("/offerAccepted/bank3/{id}")
    @Operation(description = "od banke 3 stize poruka da su nam prihvatili ponudu")
    public ResponseEntity<MyOffer> offerAcceptedBank4(@PathVariable Long id){
        if (bankOtcService.offerAccepted(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offerDeclined/bank3/{id}")
    @Operation(description = "od banke 3 stize poruka da su nam odbili ponudu")
    public ResponseEntity<MyOffer> offerDeclinedBank4(@PathVariable Long id){
        if (bankOtcService.offerDeclined(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    ///////////////////////FRONTEND/////////////////////////////////////////////////

    @GetMapping("/getBanksStocks")
    @Operation(description = "frontend dohvata sve stocks koje su u ponudi za OTC od banke3")
    public ResponseEntity<List<BankOTCStock>> getBanksStocks(){
        return ResponseEntity.ok(this.bankOtcService.getAllStocksForBanks());
    }

    @GetMapping("/getOffers")
    @Operation(description = "frontend dohvata sve ponude koje smo dobili")
    public ResponseEntity<List<Offer>> getOffers(){
        return ResponseEntity.ok(this.bankOtcService.findAllOffers());
    }

    @GetMapping("/getOurOffers")
    @Operation(description = "dohvata sve ponude koje smo poslali")
    public ResponseEntity<List<MyOffer>> getMyOffers(){
        return ResponseEntity.ok(this.bankOtcService.getMyOffers());
    }

    @PutMapping("/refresh")
    @Operation(description = "sa frontenda, da se osveze Stock-ovi drugih banaka za OTC")
    public ResponseEntity<Offer> refreshOTC(){
        this.bankOtcService.getBankStocks();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/makeOffer")
    @Operation(description = "sa frontenda stize ponuda koju treba proslediti banic 3")
    public ResponseEntity<MyOffer> makeOffer(@RequestBody FrontendOfferDto frontendOfferDto){
        if (this.bankOtcService.makeOffer(frontendOfferDto)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/acceptOffer/{id}")
    @Operation(description = "sa frontenda nam stize koju ponudu prihvatamo, id je offer u nasoj bazi")
    public ResponseEntity<Offer> acceptOffer(@PathVariable Long id){
        if (this.bankOtcService.acceptOffer(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/declineOffer/{id}")
    @Operation(description = "sa frontenda nam stize koju ponudu odbijamo, id je offer u nasoj bazi")
    public ResponseEntity<Offer> declineOffer(@PathVariable Long id){
        if (this.bankOtcService.declineOffer(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}
