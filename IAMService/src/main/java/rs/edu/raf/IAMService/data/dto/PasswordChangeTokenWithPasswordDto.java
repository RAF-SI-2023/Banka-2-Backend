package rs.edu.raf.IAMService.data.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PasswordChangeTokenWithPasswordDto implements Serializable {
    String newPassword;
    PasswordChangeTokenDto passwordChangeTokenDto;
}
