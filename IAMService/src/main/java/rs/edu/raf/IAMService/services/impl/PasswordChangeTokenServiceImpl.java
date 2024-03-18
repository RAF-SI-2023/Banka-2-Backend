package rs.edu.raf.IAMService.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.entites.PasswordChangeToken;
import rs.edu.raf.IAMService.mapper.PasswordChangeTokenMapper;
import rs.edu.raf.IAMService.mapper.PermissionMapper;
import rs.edu.raf.IAMService.repositories.PermissionRepository;
import rs.edu.raf.IAMService.repositories.ResetPasswordTokenRepository;
import rs.edu.raf.IAMService.services.PasswordChangeTokenService;

import java.util.Optional;

@Service
public class PasswordChangeTokenServiceImpl  implements PasswordChangeTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final PasswordChangeTokenMapper passwordChangeTokenMapper;


    public PasswordChangeTokenServiceImpl(ResetPasswordTokenRepository resetPasswordTokenRepository, PasswordChangeTokenMapper passwordChangeTokenMapper) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.passwordChangeTokenMapper = passwordChangeTokenMapper;
    }

    @Override
    public PasswordChangeToken createEntity(PasswordChangeToken passwordChangeToken) {
        return this.resetPasswordTokenRepository.save(passwordChangeToken);
    }

    @Override
    public void updateEntity(PasswordChangeToken passwordChangeToken) {

        Optional<PasswordChangeToken> token = resetPasswordTokenRepository.findByEmail(passwordChangeToken.getEmail());
        token.get().setToken(passwordChangeToken.getToken());
        token.get().setExpireTime(passwordChangeToken.getExpireTime());
        token.get().setUrlLink(passwordChangeToken.getUrlLink());
        resetPasswordTokenRepository.save(token.get());

    }

    @Override
    public PasswordChangeToken findByEmail(String email) {
        return resetPasswordTokenRepository.findByEmail(email).orElse(null);
    }
}
