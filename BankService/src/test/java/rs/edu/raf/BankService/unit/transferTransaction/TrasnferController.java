package rs.edu.raf.BankService.unit.transferTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import rs.edu.raf.BankService.controller.CardController;
import rs.edu.raf.BankService.controller.TransferTransactionController;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.ExchangeRateRepository;
import rs.edu.raf.BankService.service.TransactionService;
import rs.edu.raf.BankService.service.impl.TransactionServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TrasnferController {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private CashAccountRepository cashAccountRepository;
    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransferTransactionController trasnferController;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAllTransactionByEmail() throws Exception {
//        String email = "admin@gmail.com";
//
//        List<CashAccount> cashAccounts = new ArrayList<>();
//        cashAccounts.add(new CashAccount());
//        when(cashAccountRepository.findAllByEmail(email)).thenReturn(cashAccounts);
//
//        List<GenericTransactionDto> genericTransactionDtos = new ArrayList<>();
//        genericTransactionDtos.add(new GenericTransactionDto());
//
//        when(transactionService.getTransferTransactionsByEmail(email)).thenReturn(genericTransactionDtos);
//
//        List<GenericTransactionDto> responseEntity = trasnferController.getAllTransactionByEmail(email);
//
//        assertNotEquals(Collections.emptyList(), responseEntity);
    }

}
