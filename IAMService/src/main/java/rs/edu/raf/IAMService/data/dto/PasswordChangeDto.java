package rs.edu.raf.IAMService.data.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PasswordChangeDto implements Serializable {

    String email;
    String urlLink;


}
