package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.MarginsAccountRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsAccountResponseDto;
import rs.edu.raf.BankService.service.MarginsAccountService;

@RestController
@RequestMapping("/api/margins-account")
@RequiredArgsConstructor
@CrossOrigin
public class MarginsAccountController {

    private final MarginsAccountService marginsAccountService;

    @PostMapping
    public MarginsAccountResponseDto createMarginsAccount(
            @RequestBody MarginsAccountRequestDto marginsAccountRequestDto) {
        return marginsAccountService.createMarginsAccount(marginsAccountRequestDto);
    }

    @PutMapping("/{id}")
    public MarginsAccountResponseDto updateMarginsAccount(
            @PathVariable Long id,
            @RequestBody MarginsAccountRequestDto marginsAccountRequestDto) {
        return marginsAccountService.updateMarginsAccount(id, marginsAccountRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMarginsAccount(@PathVariable Long id) {
        marginsAccountService.deleteById(id);
    }

    @GetMapping("/{id}")
    public MarginsAccountResponseDto getMarginsAccountById(@PathVariable Long id) {
        return marginsAccountService.findById(id);
    }

    @PatchMapping("/{id}")
    public MarginsAccountResponseDto settleMarginCall(
            @PathVariable Long id,
            @RequestParam Double deposit) {
        return marginsAccountService.settleMarginCall(id, deposit);
    }
}