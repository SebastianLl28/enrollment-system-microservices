package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.response.UserSummaryResponse;

/**
 * @author Alonso
 */
public interface FindUserByUserIdUseCase {
  
  UserSummaryResponse findByUserId(Integer userId);
  
}
