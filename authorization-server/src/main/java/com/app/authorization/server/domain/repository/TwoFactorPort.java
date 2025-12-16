package com.app.authorization.server.domain.repository;

/**
 * @author Alonso
 */
public interface TwoFactorPort {
  
  String generateSecret(String username);
  
  String buildOtpAuthUrl(String username, String secret);
  
  boolean verifyCode(String secret, String code);
  
}
