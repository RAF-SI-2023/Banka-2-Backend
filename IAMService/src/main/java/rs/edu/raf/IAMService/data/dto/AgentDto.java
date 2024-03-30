package rs.edu.raf.IAMService.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class AgentDto extends UserDto {

    private BigDecimal limit;
    private BigDecimal leftOfLimit;

    public AgentDto(Long id,
                    Long dateOfBirth,
                    String email,
                    String username,
                    String phone,
                    String address,
                    List<PermissionType> permissions,
                    BigDecimal limit,
                    BigDecimal leftOfLimit) {
        super(id, dateOfBirth, email, username, phone, address, RoleType.AGENT, permissions);
        this.limit = limit;
        this.leftOfLimit = leftOfLimit;
    }
}
