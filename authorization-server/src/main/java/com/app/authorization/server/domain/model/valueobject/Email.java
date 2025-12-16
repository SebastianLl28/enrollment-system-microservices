package com.app.authorization.server.domain.model.valueobject;

import com.app.authorization.server.domain.exception.InvalidEmailException;

/**
 * @author Alonso
 */
public class Email {
  
  private final String value;
  
  public Email(String value) {
    if (!validateEmail(value)) {
      throw new InvalidEmailException("Invalid email format: " + value);
    }
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
  
  private Boolean validateEmail(String email) {
    return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
  }
}
