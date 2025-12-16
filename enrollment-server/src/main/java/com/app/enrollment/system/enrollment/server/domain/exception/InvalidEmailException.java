package com.app.enrollment.system.enrollment.server.domain.exception;

public class InvalidEmailException extends RuntimeException {
  
  public InvalidEmailException(String message) {
    super(message);
  }
}
