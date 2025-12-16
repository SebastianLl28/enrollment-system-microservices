package com.app.enrollment.system.enrollment.server.domain.exception;

public class CourseNotFoundException extends RuntimeException {
  
  public CourseNotFoundException(String message) {
    super(message);
  }
}
