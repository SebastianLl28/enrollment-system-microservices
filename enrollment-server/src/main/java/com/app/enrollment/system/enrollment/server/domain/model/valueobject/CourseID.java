package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import com.app.enrollment.system.enrollment.server.domain.exception.IllegalCourseIDException;
import java.util.Objects;

public final class CourseID {

  private final Integer id;


  public CourseID(Integer id) {
    this.id = id;
  }
  
  public Integer getValue() {
    return id;
  }

  @Override
  public String toString() {
    return "CourseID{" +
        "id=" + id +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CourseID courseID)) return false;
    return Objects.equals(id, courseID.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
