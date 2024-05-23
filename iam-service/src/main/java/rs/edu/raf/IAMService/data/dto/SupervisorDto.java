package rs.edu.raf.IAMService.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.List;

@Data
@NoArgsConstructor
public class SupervisorDto extends UserDto {

    public SupervisorDto
            (Long id,
             Long dateOfBirth,
             String email,
             String username,
             String phone,
             String address,
             List<PermissionType> permissions) {
        super(id, dateOfBirth, email, username, phone, address, RoleType.SUPERVISOR, permissions);
    }
}
