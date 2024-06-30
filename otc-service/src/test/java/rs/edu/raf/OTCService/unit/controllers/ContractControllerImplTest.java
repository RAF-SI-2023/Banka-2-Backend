package rs.edu.raf.OTCService.unit.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import rs.edu.raf.OTCService.controllers.ContractController;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;
import rs.edu.raf.OTCService.service.ContractService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ContractControllerImplTest {
    private MockMvc mockMvc;

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractController contractController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(contractController).build();
    }

    @Test
    void getAllContracts_Success() throws Exception {
        List<ContractDto> contracts = new ArrayList<>();
        contracts.add(new ContractDto(
                1L, false,
                false, "comment",
                1L, 1L,
                "12345", "description",
                "ticker", 1,
                100.0, ContractStatus.WAITING,
                1L, 2L,
                "buyer@gmail.com", "seller@gmail.com",
                ContractType.PRIVATE_CONTRACT));
        contracts.add(new ContractDto(
                2L, false,
                false, "comment",
                1L, 1L,
                "11111", "description",
                "ticker", 1,
                100000.00, ContractStatus.WAITING,
                1L, 2L,
                "buyer@gmail.com", "seller@gmail.com",
                ContractType.LEGAL_ENTITY_CONTRACT));

        when(contractService.getAllContracts()).thenReturn(contracts);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/contracts/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getAllWaitingContracts_Success() throws Exception {
        List<ContractDto> contracts = new ArrayList<>();
        contracts.add(new ContractDto(
                1L, false,
                false, "comment",
                1L, 1L,
                "12345", "description",
                "ticker", 1,
                100.0, ContractStatus.WAITING,
                1L, 2L,
                "buyer@gmail.com", "seller@gmail.com",
                ContractType.PRIVATE_CONTRACT));
        contracts.add(new ContractDto(
                2L, false,
                false, "comment",
                1L, 1L,
                "11111", "description",
                "ticker", 1,
                100000.00, ContractStatus.WAITING,
                1L, 2L,
                "buyer@gmail.com", "seller@gmail.com",
                ContractType.LEGAL_ENTITY_CONTRACT));
        contracts.add(new ContractDto(
                3L, false,
                false, "comment",
                1L, 1L,
                "11111", "description",
                "ticker", 1,
                123.00, ContractStatus.APPROVED,
                1L, 2L,
                "buyer@gmail.com", "seller@gmail.com",
                ContractType.LEGAL_ENTITY_CONTRACT));

        when(contractService.getAllWaitingContracts()).thenReturn(
                contracts.stream()
                        .filter(x -> x.getContractStatus() == ContractStatus.WAITING)
                        .collect(Collectors.toList()));

        // Perform GET request and validate response
        mockMvc.perform(get("/api/contracts/all-waiting")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    ContractDto getTestContractDto() {
        return new ContractDto(
                1L, false,
                false, "comment",
                1L, 1L,
                "12345", "description",
                "ticker", 1,
                100.0, ContractStatus.WAITING,
                1L, 2L,
                "buyer@gmail.com", "seller@gmail.com",
                ContractType.PRIVATE_CONTRACT);
    }

    @Test
    void getContractById_Success() throws Exception {
        ContractDto contractDto = getTestContractDto();

        when(contractService.getContractById(anyLong())).thenReturn(contractDto);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/contracts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.buyersEmail").value(contractDto.getBuyersEmail()));
    }

    @Test
    void getContractById_Exception() throws Exception {
        String errorMessage = "Contract id does not exist";

        when(contractService.getContractById(anyLong())).thenThrow(
                new RuntimeException(errorMessage));

        // Perform GET request and validate response
        mockMvc.perform(get("/api/contracts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void createContract_Success() throws Exception {
        ContractDto contractDto = getTestContractDto();

        when(contractService.createContract(any(ContractDto.class))).thenReturn(contractDto);
        // Perform GET request and validate response
        mockMvc.perform(post("/api/contracts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(contractDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.buyersEmail").value(contractDto.getBuyersEmail()));
    }

    @Test
    void createContract_Exception() throws Exception {
        // Perform GET request and validate response
        mockMvc.perform(post("/api/contracts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("BAD STRING"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sellerApproveContractById_Success() throws Exception {
        ContractDto contractDto = getTestContractDto();

        when(contractService.sellerApproveContractById(1L)).thenReturn(contractDto);
        // Perform GET request and validate response
        mockMvc.perform(put("/api/contracts/approve-seller/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.buyersEmail").value(contractDto.getBuyersEmail()));
    }

    @Test
    void sellerApproveContractById_Exception() throws Exception {
        when(contractService.sellerApproveContractById(1L)).thenThrow(
                new RuntimeException("Error"));
        // Perform GET request and validate response
        mockMvc.perform(put("/api/contracts/approve-seller/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void bankApproveContractById_Success() throws Exception {
        ContractDto contractDto = getTestContractDto();

        when(contractService.bankApproveContractById(1L)).thenReturn(contractDto);
        // Perform GET request and validate response
        mockMvc.perform(put("/api/contracts/approve-bank/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.buyersEmail").value(contractDto.getBuyersEmail()));
    }

    @Test
    void bankApproveContractById_Exception() throws Exception {
        when(contractService.bankApproveContractById(1L)).thenThrow(
                new RuntimeException("Error"));
        // Perform GET request and validate response
        mockMvc.perform(put("/api/contracts/approve-bank/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void sellerDenyContractById_Success() throws Exception {
        ContractDto contractDto = getTestContractDto();
        String comment = "TestComment123";

        when(contractService.sellerDenyContractById(1L, comment)).thenReturn(contractDto);
        // Perform GET request and validate response
        mockMvc.perform(put("/api/contracts/deny-seller/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comment))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.buyersEmail").value(contractDto.getBuyersEmail()));
    }

    @Test
    void sellerDenyContractById_Exception() throws Exception {
        String comment = "TestComment123";

        when(contractService.sellerDenyContractById(1L, comment)).thenThrow(
                new RuntimeException("Error"));
        // Perform GET request and validate response
        mockMvc.perform(put("/api/contracts/deny-seller/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comment))
                .andExpect(status().isNotFound());
    }

    @Test
    void bankDenyContractById_Success() throws Exception {
        ContractDto contractDto = getTestContractDto();
        String comment = "TestComment123";

        when(contractService.bankDenyContractById(1L, comment)).thenReturn(contractDto);
        // Perform GET request and validate response
        mockMvc.perform(put("/api/contracts/deny-bank/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comment))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.buyersEmail").value(contractDto.getBuyersEmail()));
    }

    @Test
    void bankDenyContractById_Exception() throws Exception {
        String comment = "TestComment123";

        when(contractService.bankDenyContractById(1L, comment)).thenThrow(
                new RuntimeException("Error"));
        // Perform GET request and validate response
        mockMvc.perform(put("/api/contracts/deny-bank/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comment))
                .andExpect(status().isNotFound());
    }
}
