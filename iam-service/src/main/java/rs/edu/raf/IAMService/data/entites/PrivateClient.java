package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.List;

@Entity
@DiscriminatorValue("PrivateClient")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateClient extends User {

    private String name;
    private String surname;
    private String gender;
    private String primaryAccountNumber;

    public PrivateClient(
            Long dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            List<Permission> permissions,
            String name,
            String surname,
            String gender,
            String primaryAccountNumber
    ) {
        super(dateOfBirth, email, username, phone, address, new Role(RoleType.USER), permissions);
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.primaryAccountNumber = primaryAccountNumber;
    }
}
