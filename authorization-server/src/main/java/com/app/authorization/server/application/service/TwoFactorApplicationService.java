package com.app.authorization.server.application.service;

import com.app.authorization.server.application.dto.command.TwoFactorValidateCommand;
import com.app.authorization.server.application.dto.command.VerifyTwoFactorCommand;
import com.app.authorization.server.application.dto.response.TwoFactorConfirmResponse;
import com.app.authorization.server.application.dto.response.TwoFactorInitResponse;
import com.app.authorization.server.application.dto.response.VerifyTwoFactorResponse;
import com.app.authorization.server.application.port.in.TwoFactorSetupUseCase;
import com.app.authorization.server.application.port.in.TwoFactorValidateUseCase;
import com.app.authorization.server.application.port.in.VerifyTwoFactorUseCase;
import com.app.authorization.server.domain.exception.InvalidTwoFactorCodeException;
import com.app.authorization.server.domain.exception.InvalidTwoFactorTokenException;
import com.app.authorization.server.domain.exception.TwoFactorNotInitiatedException;
import com.app.authorization.server.domain.exception.UserNotFoundException;
import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.UserAccessProfile;
import com.app.authorization.server.domain.repository.AccessTokenPort;
import com.app.authorization.server.domain.repository.TwoFactorPort;
import com.app.authorization.server.domain.repository.TwoFactorTokenPort;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.domain.repository.UserRepository;
import com.app.common.annotation.UseCase;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alonso
 */
@UseCase
public class TwoFactorApplicationService implements TwoFactorSetupUseCase, TwoFactorValidateUseCase,
  VerifyTwoFactorUseCase {
  
  private final UserRepository userRepository;
  private final TwoFactorPort twoFactorPort;
  private final TwoFactorTokenPort twoFactorTokenPort;
  private final AccessTokenPort accessTokenPort;
  private final UserAccessProfilePort userAccessProfilePort;
  
  public TwoFactorApplicationService(UserRepository userRepository, TwoFactorPort twoFactorPort,
    TwoFactorTokenPort twoFactorTokenPort, AccessTokenPort accessTokenPort,
    UserAccessProfilePort userAccessProfilePort) {
    this.userRepository = userRepository;
    this.twoFactorPort = twoFactorPort;
    this.twoFactorTokenPort = twoFactorTokenPort;
    this.accessTokenPort = accessTokenPort;
    this.userAccessProfilePort = userAccessProfilePort;
  }
  
  @Override
  @Transactional
  public TwoFactorInitResponse initiate(String username) {
    
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UserNotFoundException(username));
    
    String secret = twoFactorPort.generateSecret(user.getUsername());
    
    user.initTwoFactor(secret);
    
    userRepository.updateTwoFactor(user.getId(), user.getTwoFactorEnabled(), user.getTwoFactorSecret());
    
    String otpAuthUrl = twoFactorPort.buildOtpAuthUrl(user.getUsername(), secret);
    
    return new TwoFactorInitResponse(secret, otpAuthUrl);
    
  }
  
  
  @Override
  @Transactional
  public TwoFactorConfirmResponse validate(String username,
    TwoFactorValidateCommand twoFactorValidateCommand) {
    
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UserNotFoundException(username));
    
    if (user.getTwoFactorSecret() == null) {
      throw new TwoFactorNotInitiatedException("2FA not initiated for this user");
    }
    
    boolean valid = twoFactorPort.verifyCode(user.getTwoFactorSecret(),
      twoFactorValidateCommand.code());
    
    if (!valid) {
      throw new InvalidTwoFactorCodeException("Invalid 2FA code");
    }
    
    user.enableTwoFactor();
    
    userRepository.updateTwoFactor(user.getId(), user.getTwoFactorEnabled(), user.getTwoFactorSecret());
    
    return new TwoFactorConfirmResponse(true);
  }
  
  @Override
  public VerifyTwoFactorResponse verify(VerifyTwoFactorCommand command) {
    String code = command.code();
    String tempToken = command.tempToken();
    
    if (!twoFactorTokenPort.isTwoFactorToken(tempToken)) {
      throw new InvalidTwoFactorTokenException("Invalid temporary 2FA token");
    }
    
    String username = accessTokenPort.extractUsername(tempToken);
    
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UserNotFoundException(username));
    
    boolean valid = twoFactorPort.verifyCode(user.getTwoFactorSecret(), code);
    
    if (!valid) {
      throw new InvalidTwoFactorCodeException("Invalid 2FA code");
    }
    
    UserAccessProfile profile = userAccessProfilePort.loadByUsername(username);
    
    String newAccessToken = accessTokenPort.generateToken(user.getUsername(), profile.getPermissions());
    
    return new VerifyTwoFactorResponse(newAccessToken, username);
    
  }
  
}
