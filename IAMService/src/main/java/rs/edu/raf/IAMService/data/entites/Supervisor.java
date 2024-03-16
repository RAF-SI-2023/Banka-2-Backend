package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.List;

@Entity
@DiscriminatorValue("Supervisor")
@Data
@NoArgsConstructor
public class Supervisor extends User {

    public Supervisor(
            Long dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            List<Permission> permissions
    ) {
        super(dateOfBirth, email, username, phone, address, new Role(RoleType.SUPERVISOR), permissions);
    }
}
