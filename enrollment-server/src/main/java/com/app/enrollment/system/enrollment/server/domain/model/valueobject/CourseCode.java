package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseCodeException;
import java.util.Objects;
import java.util.regex.Pattern;

/** ej. CS101, MAT-205, INF300 */
public final class CourseCode {
  private static final Pattern PATTERN = Pattern.compile("^[A-Z]{2,6}-?[0-9]{2,4}$");
  private final String value;

  public CourseCode(String raw) {
    if (raw == null) throw new InvalidCourseCodeException("course code es obligatorio");
    String s = raw.trim().toUpperCase();
    if (!PATTERN.matcher(s).matches()) throw new InvalidCourseCodeException("course code inválido: " + raw);
    this.value = s;
  }

  public String getValue() { return value; }

  public static CourseCode of(String raw) { return new CourseCode(raw); }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CourseCode that)) return false;
    return value.equals(that.value);
  }
  @Override public int hashCode() { return Objects.hash(value); }
  @Override public String toString() { return value; }
}
