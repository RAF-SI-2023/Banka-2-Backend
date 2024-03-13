package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.*;
import lombok.*;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateOfBirth;
    @Column(unique = true, nullable = false)
    private String email;
    private String username;
    private String phone;
    private String address;
    private String password;
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role = new Role(RoleType.USER);

    @ManyToMany
    @JoinTable(
            name = "users_permissions", // intermediate table name
            joinColumns = @JoinColumn(name = "user_id"), // column name in the intermediate table pointing to User primary key
            inverseJoinColumns = @JoinColumn(name = "permission_id") // column name in the intermediate table pointing to Permission primary key
    )
    private List<Permission> permissions = new ArrayList<>();

    public User(
            Date dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            Role role,
            List<Permission> permissions,
            boolean active
    ) {
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.permissions = permissions;
        this.active = active;
    }

}
