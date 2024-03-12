package rs.edu.raf.IAMService.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 32;
    private static final int MIN_DIGITS = 2;

    public  boolean isValid(String password) {

        if (password == null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return false;
        }

        int digitCount = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }

        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])");
        Matcher matcher = pattern.matcher(password);

        return digitCount >= MIN_DIGITS && matcher.find();
    }
}
