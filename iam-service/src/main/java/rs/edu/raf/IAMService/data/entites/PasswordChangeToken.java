package rs.edu.raf.IAMService.data.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private long expireTime;
    @Column(unique = true, nullable = false)
    private String email;
    private String urlLink;

  public PasswordChangeToken(String token, long expireTime, String email, String urlLink){
        this.token=token;
        this.expireTime=expireTime;
        this.email=email;
        this.urlLink=urlLink;
    }

}
