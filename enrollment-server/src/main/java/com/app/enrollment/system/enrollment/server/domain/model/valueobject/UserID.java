package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import java.util.Objects;

/**
 * @author Alonso
 */
public final class UserID {
  private final Integer value;
  
  public UserID(Integer value) {
    this.value = value;
  }
  
  public Integer getValue() {
    return value;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof UserID userID)) {
      return false;
    }
    return Objects.equals(value, userID.value);
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
  
  @Override
  public String toString() {
    return "UserID{" +
      "value=" + value +
      '}';
  }
}
