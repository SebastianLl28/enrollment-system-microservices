package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import java.util.Objects;

/**
 * @author Alonso
 */
public final class TermID {
  
  private final Integer value;
  
  public TermID(Integer value) {
    this.value = value;
  }
  
  public Integer getValue() {
    return value;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TermID termID)) {
      return false;
    }
    return Objects.equals(value, termID.value);
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
  
  @Override
  public String toString() {
    return "TermID{" +
      "value=" + value +
      '}';
  }
}
