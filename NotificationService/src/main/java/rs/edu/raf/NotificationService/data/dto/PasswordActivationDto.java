package rs.edu.raf.NotificationService.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordActivationDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String activationUrl;
}
