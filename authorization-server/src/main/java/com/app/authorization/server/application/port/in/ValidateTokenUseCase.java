package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.response.ValidateTokenResponse;

/**
 * @author Alonso
 */
public interface ValidateTokenUseCase {

  ValidateTokenResponse validateToken(String token);
  
}
