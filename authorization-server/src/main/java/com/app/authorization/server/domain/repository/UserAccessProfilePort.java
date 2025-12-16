package com.app.authorization.server.domain.repository;

import com.app.authorization.server.domain.model.UserAccessProfile;

/**
 * @author Alonso
 */
public interface UserAccessProfilePort {
  
  UserAccessProfile loadByUsername(String username);
  
}
