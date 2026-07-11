package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import java.util.Objects;

/**
 * @author Alonso
 */
public final class ClassroomID {

  private final Integer value;

  public ClassroomID(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ClassroomID)) {
      return false;
    }
    ClassroomID that = (ClassroomID) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return "ClassroomID{" +
      "value=" + value +
      '}';
  }
}
