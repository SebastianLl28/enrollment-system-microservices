package com.app.authorization.server.domain.exception;

public class InvalidCredentialsException extends RuntimeException {

  public InvalidCredentialsException() {
    super("Invalid credentials provided");
  }
}
