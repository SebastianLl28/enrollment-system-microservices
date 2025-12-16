package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import java.util.Objects;

/**
 * @author Alonso
 */
public final class CareerID {
  
  private final Integer id;
  
  public CareerID(Integer value) {
    if (value == null || value <= 0) {
      throw new IllegalArgumentException("CareerID must be a positive non-null value.");
    }
    this.id = value;
  }
  
  public Integer getValue() {
    return id;
  }
  
  @Override
  public String toString() {
    return "CareerID{" +
      "id=" + id +
      '}';
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CareerID careerID)) return false;
    return Objects.equals(id, careerID.id);
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
  
}
