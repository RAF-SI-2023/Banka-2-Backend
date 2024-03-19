package rs.edu.raf.IAMService.services.impl;

import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.RoleDto;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.mapper.RoleMapper;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.services.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDto> getAllRoles() {
        try{
            List<Role> roles=roleRepository.findAll();
            if(roles.isEmpty()) throw new RuntimeException("Role repository is empty.");

            return roles.stream().map(this.roleMapper::roleToRoleDto).collect(Collectors.toList());
        } catch(Exception e){
            throw new RuntimeException("Roles not retrieved.", e);
        }
    }
}
