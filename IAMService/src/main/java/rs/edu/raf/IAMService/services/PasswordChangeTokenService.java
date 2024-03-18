package rs.edu.raf.IAMService.services;

import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.PasswordChangeToken;

public interface PasswordChangeTokenService {

    PasswordChangeToken createEntity(PasswordChangeToken passwordChangeToken);


    void updateEntity(PasswordChangeToken passwordChangeToken);

    PasswordChangeToken findByEmail(String email);
}
