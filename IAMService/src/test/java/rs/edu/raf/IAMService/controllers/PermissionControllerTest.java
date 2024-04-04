package rs.edu.raf.IAMService.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.edu.raf.IAMService.data.dto.PermissionDto;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.services.PermissionService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PermissionControllerTest {
    private MockMvc mockMvc;
    @Mock
    private PermissionService permissionService;
    @InjectMocks
    private PermissionController permissionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(permissionController).build();
    }

    @Test
    void findAllPermissions_success() throws Exception {
        List<PermissionDto> permissionDtos = new ArrayList<>();
        permissionDtos.add(
                new PermissionDto(
                        1L,
                        PermissionType.PERMISSION_1
                )
        );
        permissionDtos.add(
                new PermissionDto(
                        2L,
                        PermissionType.PERMISSION_2
                )
        );
        permissionDtos.add(
                new PermissionDto(
                        3L,
                        PermissionType.PERMISSION_3
                )
        );
        permissionDtos.add(
                new PermissionDto(
                        4L,
                        PermissionType.PERMISSION_4
                )
        );

        when(permissionService.getAll()).thenReturn(permissionDtos);

        mockMvc.perform(get("/api/permissions/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].permissionType").value("PERMISSION_1"))
                .andExpect(jsonPath("$[1].permissionType").value("PERMISSION_2"))
                .andExpect(jsonPath("$[2].permissionType").value("PERMISSION_3"))
                .andExpect(jsonPath("$[3].permissionType").value("PERMISSION_4"));

    }

    @Test
    void findAllPermissions_exception() throws Exception {
        when(permissionService.getAll()).thenThrow(new RuntimeException("Error occurred"));

        mockMvc.perform(get("/api/permissions/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string("Error occurred"));
    }
}
