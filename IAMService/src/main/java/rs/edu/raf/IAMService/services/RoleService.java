package rs.edu.raf.IAMService.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.RoleDto;

import java.util.List;

@Service
public interface RoleService {
    List<RoleDto> getAllRoles();


}
