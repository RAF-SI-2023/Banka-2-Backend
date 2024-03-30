package rs.edu.raf.NotificationService.data.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileActivationCodeDto {

    private String email;
    private String subject;
    private String code;
}
