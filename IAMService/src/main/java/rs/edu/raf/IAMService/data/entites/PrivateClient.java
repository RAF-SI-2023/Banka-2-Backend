package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;
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
            Date dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            Role role,
            List<Permission> permissions,
            String name,
            String surname,
            String gender,
            String primaryAccountNumber
    ) {
        super(dateOfBirth, email, username, phone, address, role, permissions);
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.primaryAccountNumber = primaryAccountNumber;
    }
}
