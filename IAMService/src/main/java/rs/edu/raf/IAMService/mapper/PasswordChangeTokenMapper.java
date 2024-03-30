package rs.edu.raf.IAMService.mapper;


import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
import rs.edu.raf.IAMService.data.entites.PasswordChangeToken;

@Component
public class PasswordChangeTokenMapper {
    public PasswordChangeTokenDto passwordChangeTokenToPasswordChangeTokenDto(PasswordChangeToken passwordChangeToken) {
        return new PasswordChangeTokenDto(
                passwordChangeToken.getToken(),
                passwordChangeToken.getExpireTime(),
                passwordChangeToken.getEmail(),
                passwordChangeToken.getUrlLink());
    }
    public PasswordChangeToken passwordChangeTokenDtoToPasswordChangeToken(PasswordChangeTokenDto passwordChangeTokenDto) {
        return new PasswordChangeToken(
                passwordChangeTokenDto.getToken(),
                passwordChangeTokenDto.getExpireTime(),
                passwordChangeTokenDto.getEmail(),
                passwordChangeTokenDto.getUrlLink());
    }

}
