package com.app.authorization.server.domain.exception;

public class InvalidTwoFactorTokenException extends RuntimeException {
  
  public InvalidTwoFactorTokenException(String message) {
    super(message);
  }
}
