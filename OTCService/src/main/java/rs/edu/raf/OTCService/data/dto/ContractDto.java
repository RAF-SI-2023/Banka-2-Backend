package rs.edu.raf.OTCService.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ContractDto {
    private Long id;
    private Boolean bankConfirmation;
    private Boolean sellerConfirmation;
    private String comment;
    private Long dateTimeCreated;
    private Long dateTimeRealized;
    private String contractNumber; //videiti da li je potreban ili je dovoljan id
    private String description;


    private String ticker; //i.e. AAPL  might be listingName or smtn
    private Integer volume;
    private Double totalPrice; //U dinarima iz nekog razloga
    private ContractStatus contractStatus;
    private Long buyersPIB;
    private Long sellersPIB;
    private String buyersEmail;
    private String sellersEmail;
    private ContractType contractType;
}
