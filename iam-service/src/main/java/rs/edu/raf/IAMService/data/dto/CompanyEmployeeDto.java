package rs.edu.raf.IAMService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEmployeeDto extends UserDto {
    private Long pib;

    public CompanyEmployeeDto(Long id, Long dateOfBirth, String email, String username, String phone, String address, Long pib) {
        super(id, dateOfBirth, email, username, phone, address, RoleType.USER, new ArrayList<>());
        this.pib = pib;
    }
}
