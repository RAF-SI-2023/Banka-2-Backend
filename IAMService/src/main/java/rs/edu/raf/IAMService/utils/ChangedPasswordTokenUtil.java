package rs.edu.raf.IAMService.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
import rs.edu.raf.IAMService.data.dto.UserDto;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
@Getter
public class ChangedPasswordTokenUtil {

    private final int tokenLength;

    public ChangedPasswordTokenUtil() {
        this.tokenLength = 16;
    }

    public PasswordChangeTokenDto generateToken(UserDto userDto, String baseURL) {

        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[tokenLength];
        random.nextBytes(tokenBytes);


        return new PasswordChangeTokenDto(Base64.getUrlEncoder().encodeToString(tokenBytes), System.currentTimeMillis(), userDto.getEmail(), baseURL + Base64.getUrlEncoder().encodeToString(tokenBytes));
    }

    public boolean isTokenValid(PasswordChangeTokenDto passwordChangeTokenDto) {
        long expiryTime = passwordChangeTokenDto.getExpireTime();


        return expiryTime > System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10);

    }


}
