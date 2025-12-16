package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.response.MyViewResponse;

/**
 * @author Alonso
 */
public interface RbacMyViewUseCase {
  
  MyViewResponse getMyViews(String username);
  
}
