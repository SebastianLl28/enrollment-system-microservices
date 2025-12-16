package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCarreerException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseCodeException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseNameException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCreditsException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLevelException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseCode;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Credits;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SemesterLevel;
import java.time.Instant;
import java.util.Objects;

/**
 * @author Alonso
 */
public class Course {
  
  private final CourseID id;
  private final CareerID careerId;
  private final CourseCode code;
  private final String name;
  private final String description;
  private final Credits credits ;
  private final SemesterLevel semesterLevel;
  private final Instant registrationDate;
  private final boolean active;
  
  private Course(CourseID id, CareerID careerId, CourseCode code, String name, String description,
    Credits credits, SemesterLevel semesterLevel, Instant registrationDate, boolean active) {
    this.id = id;
    this.careerId = careerId;
    this.code = code;
    this.name = name;
    this.description = description;
    this.credits = credits;
    this.semesterLevel = semesterLevel;
    this.registrationDate = registrationDate;
    this.active = active;
  }
  
  public static Course create(
    CareerID careerId,
    CourseCode code,
    String name,
    String description,
    Credits credits,
    SemesterLevel semesterLevel,
    Instant now
  ) {
    Objects.requireNonNull(now, "clock");
    validateRequired(careerId, code, name, credits, semesterLevel);
    return new Course(
      null,
      careerId,
      code,
      trim(name),
      trimOrNull(description),
      credits,
      semesterLevel,
      now,
      true
    );
  }
  
  public static Course rehydrate(
    CourseID id,
    CareerID careerId,
    CourseCode code,
    String name,
    String description,
    Credits credits,
    SemesterLevel semesterLevel,
    Instant registrationDate,
    boolean active
  ) {
    return new Course(
      id,
      careerId,
      code,
      name,
      description,
      credits,
      semesterLevel,
      registrationDate,
      active
    );
  }
  
  private static void validateRequired(CareerID careerId, CourseCode code, String name, Credits credits, SemesterLevel level) {
    if (careerId == null) throw new InvalidCarreerException("careerId es obligatorio.");
    if (code == null) throw new InvalidCourseCodeException("code es obligatorio.");
    if (isBlank(name)) throw new InvalidCourseNameException("name es obligatorio.");
    if (credits == null) throw new InvalidCreditsException("credits es obligatorio.");
    if (level == null) throw new InvalidSemesterLevelException("semesterLevel es obligatorio.");
  }
  
  public CourseID getId() {
    return id;
  }
  
  public CareerID getCareerId() {
    return careerId;
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
  
  public SemesterLevel getSemesterLevel() {
    return semesterLevel;
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
