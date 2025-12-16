package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.response.TwoFactorInitResponse;

/**
 * @author Alonso
 */
public interface TwoFactorSetupUseCase {
  
  TwoFactorInitResponse initiate(String username);
  
}
