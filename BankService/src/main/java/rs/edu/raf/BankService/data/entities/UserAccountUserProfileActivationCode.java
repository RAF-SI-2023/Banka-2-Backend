package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity
@Table(name = "userAccountUserProfileActivationCodes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountUserProfileActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber; // treba da bude unique
    private String code;
    private String expirationDateTime;

    public UserAccountUserProfileActivationCode(String accountNumber, String code) {
        this.accountNumber = accountNumber;
        this.code = code;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        this.expirationDateTime = String.valueOf(calendar.getTime().getTime());
    }

    public boolean isExpired() {
        return Calendar.getInstance().getTime().getTime() > Long.parseLong(expirationDateTime);
    }
}
