package com.app.authorization.server.domain.repository;

import com.app.authorization.server.domain.model.Role;
import com.app.authorization.server.domain.model.valueobject.RoleID;
import com.app.authorization.server.domain.model.valueobject.UserID;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alonso
 */
public interface RoleRepository {
  
  Role save(Role role);
  
  Optional<Role> findById(RoleID id);
  
  List<Role> findAll();
  
  void deleteById(RoleID id);
  
  void updateUserRoles(UserID userID, Set<RoleID> roleIDs);
}
