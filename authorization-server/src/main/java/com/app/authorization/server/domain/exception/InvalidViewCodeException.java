package com.app.authorization.server.domain.exception;

public class InvalidViewCodeException extends RuntimeException {
  
  public InvalidViewCodeException(String message) {
    super(message);
  }
}
