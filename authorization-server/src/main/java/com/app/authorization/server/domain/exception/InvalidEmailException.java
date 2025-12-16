package com.app.authorization.server.domain.exception;

public class InvalidEmailException extends RuntimeException {
  
  public InvalidEmailException(String email) {
    super("The email '" + email + "' is invalid.");
  }
  
}
