package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import java.util.Objects;

/**
 * @author Alonso
 */
public final class CareerOfferingID {

  private final Integer value;

  public CareerOfferingID(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CareerOfferingID)) {
      return false;
    }
    CareerOfferingID that = (CareerOfferingID) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return "CareerOfferingID{" +
      "value=" + value +
      '}';
  }
}
