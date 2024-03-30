package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@DiscriminatorValue("Agent")
@Data
@NoArgsConstructor
public class Agent extends User {

    @Column(name = "_limit", columnDefinition = "DECIMAL(38, 2)")
    private BigDecimal limit;

    @Column(name = "left_of_limit", columnDefinition = "DECIMAL(38, 2)")
    private BigDecimal leftOfLimit;

    public Agent(
            Long dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            List<Permission> permissions,
            BigDecimal limit,
            BigDecimal leftOfLimit
    ) {
        super(dateOfBirth, email, username, phone, address, new Role(RoleType.AGENT), permissions);
        this.limit = limit;
        this.leftOfLimit = leftOfLimit;
    }
}
