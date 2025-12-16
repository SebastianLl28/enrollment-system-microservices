package com.app.authorization.server.application.mapper;

import com.app.authorization.server.application.dto.response.RegisterUserResponse;
import com.app.authorization.server.application.dto.response.UIViewResponse;
import com.app.authorization.server.application.dto.response.UserRbacResponse;
import com.app.authorization.server.application.dto.response.UserResponse;
import com.app.authorization.server.application.dto.response.UserSummaryResponse;
import com.app.authorization.server.domain.model.User;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class UserMapper {
  
  public RegisterUserResponse toRegisterUserResponse(String username) {
    return new RegisterUserResponse(username);
  }
  
  
  public UserResponse toUserResponse(User user) {
    return new UserResponse(
      user.getId().getValue(),
      user.getUsername(),
      user.getEmail(),
      user.getFullName(),
      user.requiresTwoFactorAuth(),
      user.hasPassword()
    );
  }
  
  public UserSummaryResponse toUserSummaryResponse(User user) {
    return new UserSummaryResponse(user.getId().getValue(), user.getUsername(), user.getFullName());
  }
  
  public UserRbacResponse toUserRbacResponse(User user, Set<String> permissions,
    Set<UIViewResponse> uiViewsResponses) {
    
    return new UserRbacResponse(
      user.getId().getValue(),
      user.getUsername(),
      user.getEmail(),
      user.getFullName(),
      user.requiresTwoFactorAuth(),
      user.hasPassword(),
      permissions,
      uiViewsResponses
    );
  }
}
