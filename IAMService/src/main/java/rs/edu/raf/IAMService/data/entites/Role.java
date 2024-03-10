package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.*;
import lombok.*;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleType roleType;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public Role(RoleType roleType) {
        this.roleType = roleType;
    }

}
