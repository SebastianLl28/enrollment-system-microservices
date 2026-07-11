package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseCodeException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseNameException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCreditsException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseCode;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Credits;
import java.time.Instant;
import java.util.Objects;

/**
 * @author Alonso
 */
public class Course {

  private final CourseID id;
  private final CourseCode code;
  private final String name;
  private final String description;
  private final Credits credits ;
  private final Instant registrationDate;
  private final boolean active;

  private Course(CourseID id, CourseCode code, String name, String description,
    Credits credits, Instant registrationDate, boolean active) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.description = description;
    this.credits = credits;
    this.registrationDate = registrationDate;
    this.active = active;
  }

  public static Course create(
    CourseCode code,
    String name,
    String description,
    Credits credits,
    Instant now
  ) {
    Objects.requireNonNull(now, "clock");
    validateRequired(code, name, credits);
    return new Course(
      null,
      code,
      trim(name),
      trimOrNull(description),
      credits,
      now,
      true
    );
  }

  public static Course rehydrate(
    CourseID id,
    CourseCode code,
    String name,
    String description,
    Credits credits,
    Instant registrationDate,
    boolean active
  ) {
    return new Course(
      id,
      code,
      name,
      description,
      credits,
      registrationDate,
      active
    );
  }

  private static void validateRequired(CourseCode code, String name, Credits credits) {
    if (code == null) throw new InvalidCourseCodeException("code es obligatorio.");
    if (isBlank(name)) throw new InvalidCourseNameException("name es obligatorio.");
    if (credits == null) throw new InvalidCreditsException("credits es obligatorio.");
  }

  public CourseID getId() {
    return id;
  }

  public CourseCode getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Credits getCredits() {
    return credits;
  }

  public Instant getRegistrationDate() {
    return registrationDate;
  }

  public boolean isActive() {
    return active;
  }

  private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
  private static String trim(String s) { return s.trim(); }
  private static String trimOrNull(String s) { return s == null ? null : s.trim(); }
}
