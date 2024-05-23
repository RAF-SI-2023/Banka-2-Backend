package rs.edu.raf.IAMService.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CompanyDto {

    private Long id;
    private String companyName;
    private String faxNumber;
    private String phoneNumber;
    private Long pib;
    private Integer registryNumber;
    private Integer identificationNumber;
    private Integer activityCode;
    private String address;

    public CompanyDto(Long id, String companyName, String faxNumber, String phoneNumber, Long pib, Integer registryNumber, Integer identificationNumber, Integer activityCode, String address) {
        this.id = id;
        this.companyName = companyName;
        this.faxNumber = faxNumber;
        this.phoneNumber = phoneNumber;
        this.pib = pib;
        this.registryNumber = registryNumber;
        this.identificationNumber = identificationNumber;
        this.activityCode = activityCode;
        this.address = address;
    }

}
