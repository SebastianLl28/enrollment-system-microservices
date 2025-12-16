package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.CreatePermissionCommand;
import com.app.authorization.server.application.dto.command.UpdatePermissionCommand;
import com.app.authorization.server.application.dto.response.PermissionResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface RbacPermissionUseCase {
  
  PermissionResponse createPermission(CreatePermissionCommand command);
  
  PermissionResponse updatePermission(UpdatePermissionCommand command);
  
  void deletePermission(Integer id);
  
  PermissionResponse getPermission(Integer id);
  
  List<PermissionResponse> listPermissions();
  
}
