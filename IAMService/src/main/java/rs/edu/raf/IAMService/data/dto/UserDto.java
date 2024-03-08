package rs.edu.raf.IAMService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.entites.Role;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private Date dateOfBirth;
    private String email;
    private String username;
    private String phone;
    private String address;
    private Role role;
    private List<Permission> permissions = new ArrayList<>();

}
