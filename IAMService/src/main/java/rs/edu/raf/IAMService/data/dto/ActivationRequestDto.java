package rs.edu.raf.IAMService.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivationRequestDto implements Serializable {
    private Long userId;
    private String email;
    private String activationUrl;
}
