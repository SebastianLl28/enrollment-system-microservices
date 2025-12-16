package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidStudentIDException;
import java.util.Objects;

public final class StudentID {
  private final Integer value;

  public StudentID(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "StudentID{" +
        "value='" + value + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof StudentID studentID)) return false;
    return Objects.equals(value, studentID.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
