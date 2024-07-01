package rs.edu.raf.OTCService.unit.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rabbitmq.client.AMQP.PROTOCOL;

import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.entity.Contract;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;
import rs.edu.raf.OTCService.mappers.ContractMapper;

public class ContractMapperTest {
    private ContractMapper mapper;
    private Long currentTimestamp = System.currentTimeMillis();

    @BeforeEach
    public void setUp() {
        mapper = new ContractMapper();
        currentTimestamp = System.currentTimeMillis();
    }

    @Test
    public void contractToContractDto_Success() {
        // Given
        Contract contract = new Contract();
        contract.setId(1l);
        contract.setBankConfirmation(true);
        contract.setSellerConfirmation(false);
        contract.setComment("y");
        contract.setDateTimeCreated(currentTimestamp);
        contract.setDateTimeRealized(currentTimestamp);
        contract.setContractNumber("123");
        contract.setDescription("description");
        contract.setTicker("ticker");
        contract.setVolume(250);
        contract.setTotalPrice(2000.0);
        contract.setContractStatus(ContractStatus.APPROVED);
        contract.setBuyersPIB(12345l);
        contract.setSellersPIB(1234l);
        contract.setBuyersEmail("buyer@gmail.com");
        contract.setSellersEmail("seller@gmail.com");
        contract.setContractType(ContractType.LEGAL_ENTITY_CONTRACT);

        // When
        ContractDto dto = mapper.contractToDto(contract);

        // Then
        assertEquals(1l, dto.getId());
        assertEquals(true, dto.getBankConfirmation());
        assertEquals(false, dto.getSellerConfirmation());
        assertEquals("y", dto.getComment());
        assertEquals(currentTimestamp, dto.getDateTimeCreated());
        assertEquals(currentTimestamp, dto.getDateTimeRealized());
        assertEquals("123", dto.getContractNumber());
        assertEquals("description", dto.getDescription());
        assertEquals("ticker", dto.getTicker());
        assertEquals(250, dto.getVolume());
        assertEquals(2000.0, dto.getTotalPrice());
        assertEquals(ContractStatus.APPROVED, dto.getContractStatus());
        assertEquals(12345l, dto.getBuyersPIB());
        assertEquals(1234l, dto.getSellersPIB());
        assertEquals("buyer@gmail.com", dto.getBuyersEmail());
        assertEquals("seller@gmail.com", dto.getSellersEmail());
        assertEquals(ContractType.LEGAL_ENTITY_CONTRACT, dto.getContractType());

    }

    @Test
    public void currencyInflationDtoToCurrencyInflation_Success() {
        // Given
        ContractDto dto = new ContractDto();
        dto.setId(1l);
        dto.setBankConfirmation(true);
        dto.setSellerConfirmation(false);
        dto.setComment("y");
        dto.setDateTimeCreated(currentTimestamp);
        dto.setDateTimeRealized(currentTimestamp);
        dto.setContractNumber("123");
        dto.setDescription("description");
        dto.setTicker("ticker");
        dto.setVolume(250);
        dto.setTotalPrice(2000.0);
        dto.setContractStatus(ContractStatus.APPROVED);
        dto.setBuyersPIB(12345l);
        dto.setSellersPIB(1234l);
        dto.setBuyersEmail("buyer@gmail.com");
        dto.setSellersEmail("seller@gmail.com");
        dto.setContractType(ContractType.LEGAL_ENTITY_CONTRACT);
        // When

        Contract contract = mapper.dtoToContract(dto);

        // Then
        assertEquals(1l, contract.getId());
        assertEquals(true, contract.getBankConfirmation());
        assertEquals(false, contract.getSellerConfirmation());
        assertEquals("y", contract.getComment());
        assertEquals(currentTimestamp, contract.getDateTimeCreated());
        assertEquals(currentTimestamp, contract.getDateTimeRealized());
        assertEquals("123", contract.getContractNumber());
        assertEquals("description", contract.getDescription());
        assertEquals("ticker", contract.getTicker());
        assertEquals(250, contract.getVolume());
        assertEquals(2000.0, contract.getTotalPrice());
        assertEquals(ContractStatus.APPROVED, contract.getContractStatus());
        assertEquals(12345l, contract.getBuyersPIB());
        assertEquals(1234l, contract.getSellersPIB());
        assertEquals("buyer@gmail.com", contract.getBuyersEmail());
        assertEquals("seller@gmail.com", contract.getSellersEmail());
        assertEquals(ContractType.LEGAL_ENTITY_CONTRACT, contract.getContractType());

    }

}
