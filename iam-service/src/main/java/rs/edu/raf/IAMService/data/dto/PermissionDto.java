package rs.edu.raf.IAMService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.IAMService.data.enums.PermissionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    private Long id;
    private PermissionType permissionType;
}
