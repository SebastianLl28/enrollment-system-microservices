package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.UpdateUserRolesCommand;
import com.app.authorization.server.application.dto.response.UserRbacResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface UpdateUserRolesUseCase {
  
  UserRbacResponse updateUserRoles(UpdateUserRolesCommand command);
  
}
