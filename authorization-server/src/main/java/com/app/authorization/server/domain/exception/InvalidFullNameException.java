package com.app.authorization.server.domain.exception;

public class InvalidFullNameException extends RuntimeException {
  
  public InvalidFullNameException(String message) {
    super(message);
  }
}
