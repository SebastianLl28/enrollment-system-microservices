package com.app.authorization.server.domain.repository;

import com.app.authorization.server.domain.model.Permission;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface PermissionRepository {
  
  Permission save(Permission permission);
  
  Optional<Permission> findById(PermissionID id);
  
  List<Permission> findAll();
  
  void deleteById(PermissionID id);
}
