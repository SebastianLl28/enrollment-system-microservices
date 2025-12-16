package com.app.authorization.server.domain.repository;

import java.util.Set;

/**
 * @author Alonso
 */
public interface AccessTokenPort {
  String generateToken(String username, Set<String> permissions);
  String extractUsername(String token);
  boolean validateToken(String token);
  
}
