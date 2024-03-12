package rs.edu.raf.IAMService.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.data.dto.UserDto;

@Service
public interface UserService extends UserDetailsService {
    UserDto findByEmail(String email);
    PrivateClientDto createPrivateClient(PrivateClientDto privateClientDtoDto);
    CorporateClientDto createCorporateClient(CorporateClientDto corporateClientDto);
    Long activateClient(String clientId, String password);
}
