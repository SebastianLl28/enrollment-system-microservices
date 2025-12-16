package com.app.authorization.server.application.service;

import com.app.authorization.server.application.dto.command.OAuthRegisterCommand;
import com.app.authorization.server.application.dto.command.LoginCommand;
import com.app.authorization.server.application.dto.response.LoginResponse;
import com.app.authorization.server.application.dto.response.UIViewResponse;
import com.app.authorization.server.application.dto.response.UserResponse;
import com.app.authorization.server.application.dto.response.ValidateTokenResponse;
import com.app.authorization.server.application.mapper.AuthMapper;
import com.app.authorization.server.application.mapper.UserMapper;
import com.app.authorization.server.application.port.in.FindOrCreateOauthUserUseCase;
import com.app.authorization.server.application.port.in.LoginUseCase;
import com.app.authorization.server.application.port.in.ValidateTokenUseCase;
import com.app.authorization.server.domain.exception.InvalidCredentialsException;
import com.app.authorization.server.domain.exception.UserNotFoundException;
import com.app.authorization.server.domain.model.UIView;
import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.UserAccessProfile;
import com.app.authorization.server.domain.repository.AccessTokenPort;
import com.app.authorization.server.domain.repository.PasswordHasher;
import com.app.authorization.server.domain.repository.TwoFactorTokenPort;
import com.app.authorization.server.domain.repository.UIViewRepository;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.domain.repository.UserRepository;
import com.app.common.annotation.UseCase;
import java.util.Set;

/**
 * @author Alonso
 */
@UseCase
public class AuthApplicationService implements LoginUseCase, ValidateTokenUseCase,
  FindOrCreateOauthUserUseCase {
  
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final AccessTokenPort accessTokenPort;
  private final AuthMapper authMapper;
  private final TwoFactorTokenPort twoFactorTokenPort;
  private final UserMapper userMapper;
  private final UserAccessProfilePort userAccessProfilePort;
  private final UIViewRepository uIViewRepository;
  
  public AuthApplicationService(UserRepository userRepository, PasswordHasher passwordHasher,
    AccessTokenPort accessTokenPort, AuthMapper authMapper, TwoFactorTokenPort twoFactorTokenPort,
    UserMapper userMapper, UserAccessProfilePort userAccessProfilePort,
    UIViewRepository uIViewRepository) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.accessTokenPort = accessTokenPort;
    this.authMapper = authMapper;
    this.twoFactorTokenPort = twoFactorTokenPort;
    this.userMapper = userMapper;
    this.userAccessProfilePort = userAccessProfilePort;
    this.uIViewRepository = uIViewRepository;
  }
  
  @Override
  public LoginResponse login(LoginCommand command) {
    String username = command.username();
    String password = command.password();
    
    User user = userRepository.findByUsername(username)
      .orElseThrow(InvalidCredentialsException::new);
    
    if (!passwordHasher.verify(password, user.getPassword().getValue())) {
      throw new InvalidCredentialsException();
    }
    
    UserAccessProfile profile = userAccessProfilePort.loadByUsername(username);
    
    Boolean twoFactorRequired = user.requiresTwoFactorAuth();
    String token = "";
    
    if (twoFactorRequired) {
      token = twoFactorTokenPort.generateTwoFactorToken(user.getUsername(),
        profile.getPermissions());
    } else {
      token = accessTokenPort.generateToken(user.getUsername(), profile.getPermissions());
    }
    
    return authMapper.toLoginResponse(user, token);
  }
  
  @Override
  public ValidateTokenResponse validateToken(String token) {
    
    boolean isValid = accessTokenPort.validateToken(token);
    
    if (!isValid) {
      return authMapper.toValidateTokenResponse(null, Set.of(), Set.of());
    }
    
    String username = accessTokenPort.extractUsername(token);
    
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UserNotFoundException(username));
    
    UserAccessProfile profile = userAccessProfilePort.loadByUsername(username);
    
    Set<UIView> uiViews = uIViewRepository.findByUserId(user.getId());
    
    Set<UIViewResponse> uiViewsResponses = uiViews.stream().map(
      view -> new UIViewResponse(view.getCode(), view.getRoute(), view.getLabel(), view.getModule(),
        view.getSortOrder(), view.isActive())).collect(java.util.stream.Collectors.toSet());
    
    return authMapper.toValidateTokenResponse(user, profile.getPermissions(), uiViewsResponses);
    
  }
  
  @Override
  public UserResponse findOrCreateOauthUser(OAuthRegisterCommand OAuthRegisterCommand) {
    User user = userRepository.findByUsername(OAuthRegisterCommand.username()).orElseGet(() -> {
      User newUser = User.create(OAuthRegisterCommand.username(), OAuthRegisterCommand.name(),
        OAuthRegisterCommand.email());
      return userRepository.save(newUser);
    });
    
    return userMapper.toUserResponse(user);
    
  }
  
}
