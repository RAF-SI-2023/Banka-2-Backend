package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.edu.raf.IAMService.data.enums.PermissionType;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private PermissionType permissionType;

    @ManyToMany(mappedBy = "permissions")
    List<User> users;


    public Permission(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}
