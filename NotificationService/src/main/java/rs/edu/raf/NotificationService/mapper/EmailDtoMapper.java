package rs.edu.raf.NotificationService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.NotificationService.data.dto.EmailDto;
import rs.edu.raf.NotificationService.data.dto.PasswordActivationDto;
import rs.edu.raf.NotificationService.data.dto.PasswordChangeDto;

@Component
public class EmailDtoMapper {
    private final String activationContent = """
            Thank you for creating an account with us. To activate your account please click on the following link:
                        
            [Activation Link: $activationUrl]
            """;
    private final String changePasswordContent = """
            You have requested to change your password. To proceed with this change, please click on the following link:
                        
            [Change Password Link: $changePasswordLink]
            """;

    public EmailDto activationEmail(PasswordActivationDto passwordActivationDto) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail(passwordActivationDto.getEmail());
        emailDto.setSubject("Activate Your Account");
        emailDto.setContent(activationContent.replaceAll("\\$activationUrl", passwordActivationDto.getActivationUrl()));
        return emailDto;
    }

    public EmailDto changePasswordEmail(PasswordChangeDto passwordChangeDto) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail(passwordChangeDto.getEmail());
        emailDto.setSubject("Change Your Password");
        emailDto.setContent(activationContent.replaceAll("@changePasswordLink", passwordChangeDto.getUrlLink()));
        return emailDto;
    }

}
