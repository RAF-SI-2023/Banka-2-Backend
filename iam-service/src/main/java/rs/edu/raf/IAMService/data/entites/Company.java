package rs.edu.raf.IAMService.data.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String faxNumber;
    private String phoneNumber;
    private String address;
    @Column(unique = true, nullable = false, updatable = false)
    private Long pib;
    @Column(unique = true, nullable = false, updatable = false)
    private Integer registryNumber;
    @Column(unique = true, nullable = false, updatable = false)
    private Integer identificationNumber; // matični broj
    private Integer activityCode; // 5 digit code (sifra delatnosti);


}
