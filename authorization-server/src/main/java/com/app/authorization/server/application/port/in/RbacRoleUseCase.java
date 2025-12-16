package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.CreateRoleCommand;
import com.app.authorization.server.application.dto.command.UpdateRoleCommand;
import com.app.authorization.server.application.dto.response.RoleResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface RbacRoleUseCase {
  
  RoleResponse createRole(CreateRoleCommand command);
  
  RoleResponse updateRole(UpdateRoleCommand command);
  
  void deleteRole(Integer id);
  
  RoleResponse getRole(Integer id);
  
  List<RoleResponse> listRoles();

}
