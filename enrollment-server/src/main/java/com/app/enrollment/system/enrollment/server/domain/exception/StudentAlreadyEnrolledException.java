package com.app.enrollment.system.enrollment.server.domain.exception;

public class StudentAlreadyEnrolledException extends RuntimeException {
  
  public StudentAlreadyEnrolledException(String message) {
    super(message);
  }
}
