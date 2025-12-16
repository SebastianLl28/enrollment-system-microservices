package com.app.authorization.server.domain.exception;

public class TwoFactorNotInitiatedException extends RuntimeException {
  
  public TwoFactorNotInitiatedException(String message) {
    super(message);
  }
}
