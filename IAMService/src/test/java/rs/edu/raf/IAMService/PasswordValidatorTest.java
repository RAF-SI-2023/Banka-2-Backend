package rs.edu.raf.IAMService;

import org.junit.jupiter.api.Test;
import rs.edu.raf.IAMService.validator.PasswordValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordValidatorTest {

    @Test
    public void testValidPassword() {
        // Create an instance of the PasswordValidator
        PasswordValidator passwordValidator = new PasswordValidator();

        // Test a valid password
        assertTrue(passwordValidator.isValid("Password123"));
    }

    @Test
    public void testInvalidPassword_Null() {
        // Create an instance of the PasswordValidator
        PasswordValidator passwordValidator = new PasswordValidator();

        // Test null password
        assertFalse(passwordValidator.isValid(null));
    }

    @Test
    public void testInvalidPassword_ShortLength() {
        // Create an instance of the PasswordValidator
        PasswordValidator passwordValidator = new PasswordValidator();

        // Test password with length less than minimum
        assertFalse(passwordValidator.isValid("Pass"));
    }

    @Test
    public void testInvalidPassword_LongLength() {
        // Create an instance of the PasswordValidator
        PasswordValidator passwordValidator = new PasswordValidator();

        // Test password with length more than maximum
        assertFalse(passwordValidator.isValid("ThisIsALongPasswordThatExceedsTheMaximumLength"));
    }

    @Test
    public void testInvalidPassword_InsufficientDigits() {
        // Create an instance of the PasswordValidator
        PasswordValidator passwordValidator = new PasswordValidator();

        // Test password with insufficient digits
        assertFalse(passwordValidator.isValid("Password"));
    }

    @Test
    public void testInvalidPassword_NoUppercaseLetter() {
        // Create an instance of the PasswordValidator
        PasswordValidator passwordValidator = new PasswordValidator();

        // Test password with no uppercase letter
        assertFalse(passwordValidator.isValid("password123"));
    }

    @Test
    public void testInvalidPassword_NoLowercaseLetter() {
        // Create an instance of the PasswordValidator
        PasswordValidator passwordValidator = new PasswordValidator();

        // Test password with no lowercase letter
        assertFalse(passwordValidator.isValid("PASSWORD123"));
    }


}