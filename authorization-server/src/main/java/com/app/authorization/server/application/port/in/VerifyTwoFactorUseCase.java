package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.VerifyTwoFactorCommand;
import com.app.authorization.server.application.dto.response.VerifyTwoFactorResponse;

/**
 * @author Alonso
 */
public interface VerifyTwoFactorUseCase {
  
  VerifyTwoFactorResponse verify(VerifyTwoFactorCommand command);

}
