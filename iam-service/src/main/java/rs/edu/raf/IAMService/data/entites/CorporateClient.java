package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.List;


@Entity
@DiscriminatorValue("CorporateClient")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorporateClient extends User {

    private String name;
    private String primaryAccountNumber;

    public CorporateClient(
            Long dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            List<Permission> permissions,
            String name,
            String primaryAccountNumber
    ) {
        super(dateOfBirth, email, username, phone, address, new Role(RoleType.USER), permissions);
        this.name = name;
        this.primaryAccountNumber = primaryAccountNumber;
    }

}
