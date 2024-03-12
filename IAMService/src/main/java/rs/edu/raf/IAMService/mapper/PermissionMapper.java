package rs.edu.raf.IAMService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.PermissionDto;
import rs.edu.raf.IAMService.data.entites.Permission;

@Component
public class PermissionMapper {
    public PermissionDto permissionToPermissionDto(Permission permission) {
        return new PermissionDto(
                permission.getId(),
                permission.getPermissionType()
        );
    }

    public Permission permissionDtoToPermission(PermissionDto permissionDto) {
        return new Permission(
                permissionDto.getPermissionType()
        );
    }
}
