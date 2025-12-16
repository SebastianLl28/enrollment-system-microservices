package com.app.authorization.server.domain.exception;

public class UserNotFoundException extends RuntimeException {
  
  public UserNotFoundException(String username) {
    super("User not found: " + username);
  }
}
