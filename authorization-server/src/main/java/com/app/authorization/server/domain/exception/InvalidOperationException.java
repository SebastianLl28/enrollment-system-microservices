package com.app.authorization.server.domain.exception;

public class InvalidOperationException extends RuntimeException {
  
  public InvalidOperationException(String message) {
    super(message);
  }
}
