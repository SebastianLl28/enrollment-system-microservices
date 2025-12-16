package com.app.authorization.server.application.service;

import com.app.authorization.server.application.dto.command.RegisterUserCommand;
import com.app.authorization.server.application.dto.response.RegisterUserResponse;
import com.app.authorization.server.application.dto.response.UIViewResponse;
import com.app.authorization.server.application.dto.response.UserRbacResponse;
import com.app.authorization.server.application.dto.response.UserResponse;
import com.app.authorization.server.application.dto.response.UserSummaryResponse;
import com.app.authorization.server.application.mapper.UserMapper;
import com.app.authorization.server.application.port.in.FindUserByUserCase;
import com.app.authorization.server.application.port.in.FindUserByUserIdUseCase;
import com.app.authorization.server.application.port.in.RegisterUserUseCase;
import com.app.authorization.server.domain.exception.UserAlreadyExistsException;
import com.app.authorization.server.domain.model.UIView;
import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.UserAccessProfile;
import com.app.authorization.server.domain.model.valueobject.Password;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.domain.repository.PasswordHasher;
import com.app.authorization.server.domain.repository.UIViewRepository;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.domain.repository.UserRepository;
import com.app.common.annotation.UseCase;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alonso
 */
@UseCase
public class UserManagementApplication implements RegisterUserUseCase, FindUserByUserCase,
  FindUserByUserIdUseCase {
  
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final UserMapper userMapper;
  private final UserAccessProfilePort userAccessProfilePort;
  private final UIViewRepository uIViewRepository;
  
  public UserManagementApplication(UserRepository userRepository, PasswordHasher passwordHasher,
    UserMapper userMapper, UserAccessProfilePort userAccessProfilePort,
    UIViewRepository uIViewRepository) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.userMapper = userMapper;
    this.userAccessProfilePort = userAccessProfilePort;
    this.uIViewRepository = uIViewRepository;
  }
  
  @Override
  public RegisterUserResponse register(RegisterUserCommand command) {
    
    userRepository.findByUsername(command.username()).ifPresent(user -> {
      throw new UserAlreadyExistsException("Username already exists");
    });
    
    userRepository.findByEmail(command.username()).ifPresent(user -> {
      throw new UserAlreadyExistsException("Email already exists");
    });
    
    Password password = Password.fromRaw(command.password(), passwordHasher);
    
    User user = User.create(command.username(), password, command.fullName(), command.email());
    
    User savedUser = userRepository.save(user);
    
    return userMapper.toRegisterUserResponse(savedUser.getUsername());
  }
  
  @Override
  public UserResponse findByUsername(String username) {
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UserAlreadyExistsException("User not found"));
    return userMapper.toUserResponse(user);
  }
  
  @Override
  public List<UserRbacResponse> findAllUsersWithRbac() {
    
    List<User> users = userRepository.findAll();
    
    return users.stream().map(user -> {
      String username = user.getUsername();
      
      UserAccessProfile profile = userAccessProfilePort.loadByUsername(username);
      
      Set<UIView> uiViews = uIViewRepository.findByUserId(user.getId());
      
      Set<UIViewResponse> uiViewsResponses = uiViews.stream().map(
          view -> new UIViewResponse(view.getCode(), view.getRoute(), view.getLabel(),
            view.getModule(), view.getSortOrder(), view.isActive()))
        .collect(java.util.stream.Collectors.toSet());
      
      return userMapper.toUserRbacResponse(user, profile.getPermissions(), uiViewsResponses);
      
    }).toList();
  }
  
  @Override
  public UserSummaryResponse findByUserId(Integer userId) {
    UserID userID = new UserID(userId);
    User user = userRepository.findById(userID)
      .orElseThrow(() -> new UserAlreadyExistsException("User not found"));
    return userMapper.toUserSummaryResponse(user);
  }
}
