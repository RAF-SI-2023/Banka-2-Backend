package rs.edu.raf.IAMService.mapper;

import org.junit.jupiter.api.Test;
import rs.edu.raf.IAMService.data.dto.PermissionDto;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.enums.PermissionType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PermissionMapperTest {
    private final PermissionMapper permissionMapper = new PermissionMapper();

    @Test
    public void permissionToPermissionDto_success() {
        Permission permission = new Permission(PermissionType.PERMISSION_1);
        permission.setId(1L);

        PermissionDto permissionDto = permissionMapper.permissionToPermissionDto(permission);

        assertEquals(permission.getId(), permissionDto.getId());
        assertEquals(permission.getPermissionType(), permissionDto.getPermissionType());
    }

    @Test
    public void permissionDtoToPermission_success() {
        PermissionDto permissionDto = new PermissionDto(1L, PermissionType.PERMISSION_1);

        Permission permission = permissionMapper.permissionDtoToPermission(permissionDto);

        assertEquals(permissionDto.getPermissionType(), permission.getPermissionType());
    }
}
