package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import java.util.Objects;

/**
 * @author Alonso
 */
public final class CourseOfferingID {
  
  private final Integer value;
  
  public CourseOfferingID(Integer value) {
    this.value = value;
  }
  
  public Integer getValue() {
    return value;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CourseOfferingID)) {
      return false;
    }
    CourseOfferingID that = (CourseOfferingID) o;
    return Objects.equals(value, that.value);
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
  
  @Override
  public String toString() {
    return "CourseOfferingID{" +
      "value=" + value +
      '}';
  }
}
