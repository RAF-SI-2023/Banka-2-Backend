package rs.edu.raf.IAMService.permissions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.IAMService.controllers.PermissionController;
import rs.edu.raf.IAMService.data.dto.PermissionDto;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.services.PermissionService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PermissionControllerUnitTest {
    @Mock
    private PermissionService permissionService;
    @InjectMocks
    private PermissionController permissionController;

//    @Test
//    void test_get_all_controller_success() {
//        List<PermissionDto> permissions = Arrays.asList(
//                new PermissionDto(1L, PermissionType.PERMISSION_1),
//                new PermissionDto(2L, PermissionType.PERMISSION_2)
//        );
//
//        when(permissionService.getAll()).thenReturn(permissions);
//
//        ResponseEntity<?> responseEntity = permissionController.getAll();
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//        List<String> expectedPermissionTypes = permissions.stream()
//                .map(permissionDto -> permissionDto.getPermissionType().toString())
//                .collect(Collectors.toList());
//
//        assertEquals(expectedPermissionTypes, responseEntity.getBody());
//    }

//    @Test
//    void test_get_all_throws_exception() {
//        when(permissionService.getAll()).thenThrow(new RuntimeException("Failed to retrieve permissions"));
//
//        ResponseEntity<?> responseEntity = permissionController.getAll();
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//        assertEquals("Failed to retrieve permission", responseEntity.getBody());
//    }

}
