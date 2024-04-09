package rs.edu.raf.NotificationService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.NotificationService.data.dto.*;

@Component
public class EmailDtoMapper {
    private static final String PASSWORD_ACTIVATION_CONTENT = """
            Thank you for creating an account with us. To activate your account please click on the following link:
                        
            [Activation Link: $activationUrl]
            """;
    private static final String PASSWORD_CHANGE_CONTENT = """
            You have requested to change your password. To proceed with this change, please click on the following link:
                        
            [Change Password Link: $changePasswordLink]
            """;
    private static final String PROFILE_ACTIVATION_CONTENT = """
            To ensure the security of your account, we require you to verify your email address. Please use the following verification code to complete the process:
                        
            Verification Code: [$verificationCode]
            """;

    private static final String TRANSACTION_VERIFICATION = """
            To ensure the security of your account, we require you to verify the transaction you have just created. Please use the following verification token to complete the process:
                        
            Verification Token: [&verificationToken]
            """;

    public EmailDto activationEmail(PasswordActivationDto passwordActivationDto) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail(passwordActivationDto.getEmail());
        emailDto.setSubject("Activate Your Password");
        emailDto.setContent(PASSWORD_ACTIVATION_CONTENT.replaceAll("\\$activationUrl", passwordActivationDto.getActivationUrl()));
        return emailDto;
    }

    public EmailDto changePasswordEmail(PasswordChangeDto passwordChangeDto) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail(passwordChangeDto.getEmail());
        emailDto.setSubject("Change Your Password");
        emailDto.setContent(PASSWORD_CHANGE_CONTENT.replaceAll("\\$changePasswordLink", passwordChangeDto.getUrlLink()));
        return emailDto;
    }

    public EmailDto profileActivationEmail(ProfileActivationCodeDto profileActivationCodeDto) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail(profileActivationCodeDto.getEmail());
        emailDto.setSubject("Activate Your Account");
        emailDto.setContent(PROFILE_ACTIVATION_CONTENT.replaceAll("\\$verificationCode", profileActivationCodeDto.getCode() + ""));
        return emailDto;
    }

    public EmailDto transactionVerification(TransferTransactionVerificationDto transferTransactionVerificationDto) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail(transferTransactionVerificationDto.getEmail());
        emailDto.setSubject("Verify Your Transaction");
        emailDto.setContent(TRANSACTION_VERIFICATION.replaceAll("\\$verificationToken", transferTransactionVerificationDto.getVerificationToken() + ""));
        return emailDto;
    }

}
