package com.app.enrollment.system.enrollment.server.domain.exception;

public class StudentNotFoundException extends RuntimeException {
  
  public StudentNotFoundException(String message) {
    super(message);
  }
}
