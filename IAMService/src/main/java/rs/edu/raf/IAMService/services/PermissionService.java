package rs.edu.raf.IAMService.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.PermissionDto;

import java.util.List;

@Service
public interface  PermissionService {
    List<PermissionDto> getAll();
}
