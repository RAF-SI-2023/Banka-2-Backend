package rs.edu.raf.IAMService.data.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PasswordChangeTokenDto {

    String token;
    long expireTime;
    String email;
    String urlLink;

}
