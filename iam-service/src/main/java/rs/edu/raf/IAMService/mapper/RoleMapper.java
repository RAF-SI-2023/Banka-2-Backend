package rs.edu.raf.IAMService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.RoleDto;
import rs.edu.raf.IAMService.data.entites.Role;

@Component
public class RoleMapper {
    public Role roleDtoToRole(RoleDto roleDto){
        return new Role(
            roleDto.getId(),
            roleDto.getRoleType()
        );
    }

    public RoleDto roleToRoleDto(Role role){
        return new RoleDto(
                role.getId(),
                role.getRoleType()
        );
    }
}
