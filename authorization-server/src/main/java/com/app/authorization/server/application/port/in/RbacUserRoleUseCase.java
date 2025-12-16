package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.UpdateUserRolesCommand;
import com.app.authorization.server.application.dto.response.UserRolesResponse;

/**
 * @author Alonso
 */
public interface RbacUserRoleUseCase {
  
  UserRolesResponse updateUserRoles(UpdateUserRolesCommand command);
  
  UserRolesResponse getUserRoles(Integer userId);

}
