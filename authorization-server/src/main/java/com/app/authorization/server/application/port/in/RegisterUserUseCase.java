package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.RegisterUserCommand;
import com.app.authorization.server.application.dto.response.RegisterUserResponse;

/**
 * @author Alonso
 */
public interface RegisterUserUseCase {
  
  RegisterUserResponse register(RegisterUserCommand command);
  
}
