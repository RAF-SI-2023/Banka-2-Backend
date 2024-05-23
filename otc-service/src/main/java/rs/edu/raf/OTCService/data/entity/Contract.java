package rs.edu.raf.OTCService.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;

@Data
@Entity
@Table(name = "contracts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean bankConfirmation = null;
    private Boolean sellerConfirmation = null;
    private String comment;
    private Long dateTimeCreated;
    private Long dateTimeRealized;
    private String contractNumber; //videiti da li je potreban ili je dovoljan id
    private String description;
    private String ticker; //i.e. AAPL  might be listingName or smtn
    private Integer volume;
    private Double totalPrice; //U dinarima iz nekog razloga

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus = ContractStatus.WAITING;


    private Long buyersPIB = null;
    private Long sellersPIB = null;
    private String buyersEmail = null;
    private String sellersEmail = null;
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

}
