package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.OAuthRegisterCommand;
import com.app.authorization.server.application.dto.response.UserResponse;

/**
 * @author Alonso
 */
public interface FindOrCreateOauthUserUseCase {
  
  UserResponse findOrCreateOauthUser(OAuthRegisterCommand OAuthRegisterCommand);
  
}
