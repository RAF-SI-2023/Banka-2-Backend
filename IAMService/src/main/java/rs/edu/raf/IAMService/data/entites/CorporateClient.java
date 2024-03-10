package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;
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
            Date dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            Role role,
            List<Permission> permissions,
            String name,
            String primaryAccountNumber
    ) {
        super(dateOfBirth, email, username, phone, address, role, permissions);
        this.name = name;
        this.primaryAccountNumber = primaryAccountNumber;
    }

}
