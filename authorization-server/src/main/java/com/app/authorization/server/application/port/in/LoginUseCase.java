package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.LoginCommand;
import com.app.authorization.server.application.dto.response.LoginResponse;

/**
 * @author Alonso
 */
public interface LoginUseCase {
  
  LoginResponse login(LoginCommand command);
  
}
