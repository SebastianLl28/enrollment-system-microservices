package com.app.authorization.server.domain.exception;

public class InvalidUsernameException extends RuntimeException {
  
  public InvalidUsernameException(String message) {
    super(message);
  }
}
