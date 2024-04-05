package rs.edu.raf.IAMService.unit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.IAMService.data.dto.RoleDto;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.mapper.RoleMapper;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.services.impl.RoleServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RoleServiceImplUnitTest {

    @InjectMocks
    private RoleServiceImpl roleService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleMapper roleMapper;

    @Test
    void testGetAllRoles_Success() {
        Role testRole1=new Role();
        Role testRole2=new Role();

        testRole1.setRoleType(RoleType.SUPERVISOR);
        testRole1.setId(1L);
        testRole2.setRoleType(RoleType.EMPLOYEE);
        testRole2.setId(2L);

        List<Role> testRoles = new ArrayList<>();
        testRoles.add(testRole1);
        testRoles.add(testRole2);

        when(roleRepository.findAll()).thenReturn(testRoles);

        when(roleMapper.roleToRoleDto(testRole1)).thenReturn(new RoleDto(testRole1.getId(),testRole1.getRoleType()));
        when(roleMapper.roleToRoleDto(testRole2)).thenReturn(new RoleDto(testRole2.getId(),testRole2.getRoleType()));

        List<RoleDto> successValue = roleService.getAllRoles();

        for(RoleDto successRoleDto:successValue){
            Boolean match=false;
            for(Role testRole:testRoles){
                if(successRoleDto.getRoleType()==testRole.getRoleType()){
                    match=true;
                    break;
                }
            }
            if(!match) fail("Role retrieval failed");
        }
    }

}
