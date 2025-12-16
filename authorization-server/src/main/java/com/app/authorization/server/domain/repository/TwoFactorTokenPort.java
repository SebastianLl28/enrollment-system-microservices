package com.app.authorization.server.domain.repository;

import java.util.Set;

/**
 * @author Alonso
 */
public interface TwoFactorTokenPort {
  String generateTwoFactorToken(String username, Set<String> permissions);
  boolean isTwoFactorToken(String token);
  
}
