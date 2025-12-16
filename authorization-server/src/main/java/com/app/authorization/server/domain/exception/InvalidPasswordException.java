package com.app.authorization.server.domain.exception;

public class InvalidPasswordException extends RuntimeException {

  public InvalidPasswordException(int minLength) {
    super("The password must be at least " + minLength + " characters long.");
  }
  
  public InvalidPasswordException(String message) {
    super(message);
  }
  
}
