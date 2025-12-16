package com.app.authorization.server.domain.exception;

public class PasswordRequiredException extends RuntimeException {
  
  public PasswordRequiredException(String message) {
    super(message);
  }
}
