package rs.edu.raf.IAMService.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.entites.Role;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
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
            Role role,
            List<Permission> permissions,
            String name,
            String surname,
            String gender,
            String primaryAccountNumber
    ) {
        super(id, dateOfBirth, email, username, phone, address, role, permissions);
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.primaryAccountNumber = primaryAccountNumber;
    }
}
