package com.app.enrollment.system.enrollment.server.domain.model.valueobject;


import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCreditsException;

public final class Credits {
  private final int value;

  public Credits(int value) {
    if (value < 1 || value > 20) throw new InvalidCreditsException("credits debe estar entre 1 y 20.");
    this.value = value;
  }

  public int getValue() { return value; }
  public static Credits of(int v) { return new Credits(v); }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Credits c)) return false;
    return value == c.value;
  }
  @Override public int hashCode() { return Integer.hashCode(value); }
  @Override public String toString() { return Integer.toString(value); }
}
