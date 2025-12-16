package com.app.authorization.server.application.mapper;

import com.app.authorization.server.application.dto.response.LoginResponse;
import com.app.authorization.server.application.dto.response.UIViewResponse;
import com.app.authorization.server.application.dto.response.ValidateTokenResponse;
import com.app.authorization.server.domain.model.User;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class AuthMapper {
  
  public LoginResponse toLoginResponse(User user, String token) {
    return new LoginResponse(token, user.getUsername(), user.requiresTwoFactorAuth());
  }
  
  public ValidateTokenResponse toValidateTokenResponse(User user, Set<String> permissions, Set<UIViewResponse> uiViews) {
    boolean isValid = user != null;
    String userName = null;
    Integer userId = null;
    if (isValid) {
      userName = user.getUsername();
      userId = user.getId().getValue();
    }
    
    return new ValidateTokenResponse(userName, isValid, userId, permissions, uiViews);
    
  }
}
