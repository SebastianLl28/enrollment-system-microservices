package com.app.authorization.server.domain.exception;

public class InvalidResourceException extends RuntimeException {
  
  public InvalidResourceException(String message) {
    super(message);
  }
}
