package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.TwoFactorValidateCommand;
import com.app.authorization.server.application.dto.response.TwoFactorConfirmResponse;

/**
 * @author Alonso
 */
public interface TwoFactorValidateUseCase {
  
  TwoFactorConfirmResponse validate(String username, TwoFactorValidateCommand twoFactorValidateCommand);
  
}
