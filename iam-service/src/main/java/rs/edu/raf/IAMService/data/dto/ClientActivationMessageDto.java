package rs.edu.raf.IAMService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientActivationMessageDto {

    private String activationUrl;
    private String email;
}
