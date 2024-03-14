package rs.edu.raf.IAMService.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.List;

@Data
@NoArgsConstructor
public class CorporateClientDto extends UserDto {

    private String name;
    private String primaryAccountNumber;


    public CorporateClientDto(
            Long id,
            Long dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            List<PermissionType> permissions,
            String name,
            String primaryAccountNumber
    ) {
        super(id, dateOfBirth, email, username, phone, address, RoleType.USER, permissions);
        this.name = name;
        this.primaryAccountNumber = primaryAccountNumber;
    }
}
