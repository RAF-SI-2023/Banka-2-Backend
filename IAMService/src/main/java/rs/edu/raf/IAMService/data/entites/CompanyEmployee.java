package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.ArrayList;

@Entity
@DiscriminatorValue("CompanyEmployee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEmployee extends User {
    private Long pib;

    public CompanyEmployee(Long dateOfBirth, String email, String username, String phone, String address, Long pib) {
        super(dateOfBirth, email, username, phone, address, new Role(RoleType.USER), new ArrayList<>());
        this.pib = pib;
    }
}
