package rs.edu.raf.IAMService.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PrivateClientDto extends UserDto {

    private String name;
    private String surname;
    private String gender;
    private String primaryAccountNumber;


    public PrivateClientDto(
            Long id,
            Date dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            List<PermissionType> permissions,
            boolean active,
            String name,
            String surname,
            String gender,
            String primaryAccountNumber
    ) {
        super(id, dateOfBirth, email, username, phone, address, RoleType.USER, permissions, active);
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.primaryAccountNumber = primaryAccountNumber;
    }
}
