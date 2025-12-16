package com.app.authorization.server.domain.repository;

/**
 * @author Alonso
 */
public interface PasswordHasher {
  
  String hash(String plainPassword);
  
  Boolean verify(String plainPassword, String hashedPassword);
  
}
