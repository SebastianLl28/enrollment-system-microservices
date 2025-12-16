package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.response.UserRbacResponse;
import com.app.authorization.server.application.dto.response.UserResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface FindUserByUserCase {
  
  UserResponse findByUsername(String username);
  
  List<UserRbacResponse> findAllUsersWithRbac();
  
}
