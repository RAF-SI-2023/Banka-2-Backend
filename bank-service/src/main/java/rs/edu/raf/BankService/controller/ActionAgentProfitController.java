package rs.edu.raf.BankService.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.BankService.service.ActionAgentProfitService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/agent-profit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ActionAgentProfitController {


    private final ActionAgentProfitService actionAgentProfitService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {

        try {
            return ResponseEntity.ok(actionAgentProfitService.getAllProfits());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @GetMapping("/all-total-profits")
    public ResponseEntity<?> getAllTotalProfitsByEmail() {

        try {
            return ResponseEntity.ok(actionAgentProfitService.getTotalProfitsByUsers());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }


}
