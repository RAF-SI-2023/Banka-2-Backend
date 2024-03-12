package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountUserProfileConnectionToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber; // treba da bude unique
    private Integer code;
    private Long expirationDateTime;

    public UserAccountUserProfileConnectionToken(String accountNumber, Integer code) {
        this.accountNumber = accountNumber;
        this.code = code;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        this.expirationDateTime = calendar.getTime().getTime();
    }

    public boolean isExpired() {
        return Calendar.getInstance().getTime().getTime() > expirationDateTime;
    }
}
