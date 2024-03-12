package rs.edu.raf.IAMService.data.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("Employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends User {

    private String name;
    private String surname;
    private String gender;
    private String position;
    private String department;

    public Employee(
            Date dateOfBirth,
            String email,
            String username,
            String phone,
            String address,
            Role role,
            List<Permission> permissions,
            boolean active,
            String name,
            String surname,
            String gender,
            String position,
            String department
    ) {
        super(dateOfBirth, email, username, phone, address, role, permissions, active);
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.position = position;
        this.department = department;
    }
}
