package com.app.authorization.server.domain.exception;

public class InvalidTwoFactorCodeException extends RuntimeException {
  
  public InvalidTwoFactorCodeException(String message) {
    super(message);
  }
}
