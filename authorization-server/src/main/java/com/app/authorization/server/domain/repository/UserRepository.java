package com.app.authorization.server.domain.repository;

import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.valueobject.UserID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface UserRepository {
  
  Optional<User> findByUsername(String username);
  
  User save(User user);
  
  Optional<User> findById(UserID id);
  
  Optional<User> findByEmail(String email);
  
  List<User> findAll();
  
  void updateTwoFactor(UserID id, boolean enabled, String secret);
}

