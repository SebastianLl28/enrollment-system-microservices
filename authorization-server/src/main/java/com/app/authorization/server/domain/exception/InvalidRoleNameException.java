package com.app.authorization.server.domain.exception;

public class InvalidRoleNameException extends RuntimeException {
  
  public InvalidRoleNameException(String message) {
    super(message);
  }
}
