package com.app.authorization.server.domain.exception;

public class TwoFactorNotInitializedException extends RuntimeException {
  
  public TwoFactorNotInitializedException(String message) {
    super(message);
  }
}
