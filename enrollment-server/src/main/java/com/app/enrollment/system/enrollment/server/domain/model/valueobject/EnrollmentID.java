package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import java.util.Objects;

/**
 * @author Alonso
 */
public final class EnrollmentID {
  
  private final Integer id;
  
  public EnrollmentID(Integer id) {
    this.id = id;
  }
  
  public Integer getValue() {
    return id;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof EnrollmentID that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
  
  @Override
  public String toString() {
    return "EnrollmentID{" +
      "id=" + id +
      '}';
  }
}
