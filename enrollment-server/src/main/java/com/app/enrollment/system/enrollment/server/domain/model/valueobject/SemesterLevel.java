package com.app.enrollment.system.enrollment.server.domain.model.valueobject;


import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLevelException;

public final class SemesterLevel {
  private final int value;

  public SemesterLevel(int value) {
    if (value < 1 || value > 20) throw new InvalidSemesterLevelException("semesterLevel debe estar entre 1 y 20.");
    this.value = value;
  }

  public int getValue() { return value; }
  public static SemesterLevel of(int v) { return new SemesterLevel(v); }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SemesterLevel s)) return false;
    return value == s.value;
  }
  @Override public int hashCode() { return Integer.hashCode(value); }
  @Override public String toString() { return Integer.toString(value); }
}
