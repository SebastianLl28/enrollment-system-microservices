package com.app.enrollment.system.enrollment.server.domain.exception;

public class CannotChangeStatusOfCancelledEnrollmentException extends RuntimeException {
  
  public CannotChangeStatusOfCancelledEnrollmentException(String message) {
    super(message);
  }
}
