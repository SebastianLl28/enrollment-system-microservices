package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidFacultyIDException;

/**
 * @author Alonso
 */
public final class FacultyID {
  
  private final Integer value;
  
  public FacultyID(Integer value) {
    if (value == null || value <= 0) {
      throw new InvalidFacultyIDException("FacultyID must be a positive integer.");
    }
    this.value = value;
  }
  
  public Integer getValue() {
    return value;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    FacultyID facultyID = (FacultyID) obj;
    return value.equals(facultyID.value);
  }
  
  @Override
  public int hashCode() {
    return value.hashCode();
  }
  
  @Override
  public String toString() {
    return "FacultyID{" + "value=" + value + '}';
  }
  
}
