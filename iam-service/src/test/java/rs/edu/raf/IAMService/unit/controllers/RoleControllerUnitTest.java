package rs.edu.raf.IAMService.unit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.IAMService.controllers.RoleController;
import rs.edu.raf.IAMService.data.dto.RoleDto;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.services.RoleService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleControllerUnitTest {
    @InjectMocks
    private RoleController roleController;
    @Mock
    private RoleService roleService;

    @Test
    void testGetAllRoles_Success() {
        List<RoleDto> roleDtos = new ArrayList<>();
        roleDtos.add(new RoleDto(1L, RoleType.SUPERVISOR));
        roleDtos.add(new RoleDto(2L, RoleType.USER));

        when(roleService.getAllRoles()).thenReturn(roleDtos);

        ResponseEntity response = roleController.getAllRoles();

        List<RoleDto> responseBody = (List<RoleDto>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        for (RoleDto testRoleDto : roleDtos) {
            Boolean match = false;
            for (RoleDto responseRoleDto : responseBody) {
                if (responseRoleDto.getRoleType() == testRoleDto.getRoleType()) {
                    match = true;
                    break;
                }
            }
            if (!match) fail("Role retrieval failed");
        }
    }

    @Test
    void testGetAllRoles_Exception() {
        when(roleService.getAllRoles()).thenThrow(new RuntimeException("Role retrieval failed"));
        ResponseEntity response = roleController.getAllRoles();
        assertEquals("Role retrieval failed", response.getBody());
    }

}
