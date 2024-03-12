package rs.edu.raf.IAMService.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.PermissionDto;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.mapper.PermissionMapper;
import rs.edu.raf.IAMService.repositories.PermissionRepository;
import rs.edu.raf.IAMService.services.PermissionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<PermissionDto> getAll() {
        try {
            List<Permission> permissions = permissionRepository.findAll();
            if (permissions == null) throw new RuntimeException("Permission repository returned null");

            List<PermissionDto> permissionDtos = permissions.stream().map(this.permissionMapper::permissionToPermissionDto).collect(Collectors.toList());

            return permissionDtos;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve permissions ", e);
        }
    }

}
