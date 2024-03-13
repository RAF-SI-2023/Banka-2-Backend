package rs.edu.raf.IAMService.permissions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.IAMService.data.dto.PermissionDto;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.mapper.PermissionMapper;
import rs.edu.raf.IAMService.repositories.PermissionRepository;
import rs.edu.raf.IAMService.services.impl.PermissionServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceUnitTests {
    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @Test
    void test_get_all_success() {
        List<Permission> permissions = new ArrayList<>();
        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setPermissionType(PermissionType.PERMISSION_1);

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setPermissionType(PermissionType.PERMISSION_2);
        permissions.add(permission1);
        permissions.add(permission2);

        when(permissionMapper.permissionToPermissionDto(permission1)).thenReturn(new PermissionDto(1L, PermissionType.PERMISSION_1));
        when(permissionMapper.permissionToPermissionDto(permission2)).thenReturn(new PermissionDto(2L, PermissionType.PERMISSION_2));

        when(permissionRepository.findAll()).thenReturn(permissions);

        List<PermissionDto> permissionDtos = permissionService.getAll();
        System.out.println("permissions" + permissions);
        System.out.println("perm dtos " + permissionDtos);

        assertEquals(permissions.size(), permissionDtos.size());

        for (int i = 0; i < permissions.size(); i++) {
            Permission permission = permissions.get(i);
            PermissionDto permissionDto = permissionDtos.get(i);

            assertEquals(permission.getId(), permissionDto.getId());
            assertEquals(permission.getPermissionType(), permissionDto.getPermissionType());
        }
    }

    @Test
    void test_get_all_throws_exception() {
        when(permissionRepository.findAll()).thenThrow(new RuntimeException("Simulated repository exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            permissionService.getAll();
        });
        assertEquals("Failed to retrieve permissions ", exception.getMessage());
        assertEquals("Simulated repository exception", exception.getCause().getMessage());
    }
}
