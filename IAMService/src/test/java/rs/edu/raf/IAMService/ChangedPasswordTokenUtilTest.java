package rs.edu.raf.IAMService;

import org.junit.jupiter.api.Test;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.utils.ChangedPasswordTokenUtil;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;

public class ChangedPasswordTokenUtilTest {


    @Test
    void testGenerateToken() {
        ChangedPasswordTokenUtil tokenUtil = new ChangedPasswordTokenUtil();
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        String baseURL = "http://localhost:8080/api/users/changePasswordSubmit/";

        PasswordChangeTokenDto tokenDto = tokenUtil.generateToken(userDto, baseURL);

        assertNotNull(tokenDto);
        assertNotNull(tokenDto.getToken());
        assertNotNull(tokenDto.getUrlLink());
        assertEquals("test@example.com", tokenDto.getEmail());
    }

    @Test
    void testIsTokenValidWithinExpiryTime() {
        ChangedPasswordTokenUtil tokenUtil = new ChangedPasswordTokenUtil();
        long currentTime = System.currentTimeMillis();
        long expiryTime = currentTime + TimeUnit.MINUTES.toMillis(10); // Token expires in 5 minutes
        PasswordChangeTokenDto tokenDto = new PasswordChangeTokenDto();
        tokenDto.setExpireTime(expiryTime);

        assertTrue(tokenUtil.isTokenValid(tokenDto));
    }

    @Test
    void testIsTokenInvalidPastExpiryTime() {
        ChangedPasswordTokenUtil tokenUtil = new ChangedPasswordTokenUtil();
        long currentTime = System.currentTimeMillis();
        long expiryTime = currentTime - TimeUnit.MINUTES.toMillis(10); // Token expired 5 minutes ago
        PasswordChangeTokenDto tokenDto = new PasswordChangeTokenDto();
        tokenDto.setExpireTime(expiryTime);

        assertFalse(tokenUtil.isTokenValid(tokenDto));
    }
}
