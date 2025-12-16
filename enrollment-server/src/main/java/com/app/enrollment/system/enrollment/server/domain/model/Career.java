package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.exception.CareerNameRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyNameRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDegreeTitleException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLengthException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DegreeTitle;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import jakarta.xml.bind.ValidationException;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * @author Alonso
 */
public class Career {
  
  private final CareerID id;
  private final FacultyID facultyId;
  private final String name;
  private final String description;
  private final int semesterLength;
  private final DegreeTitle degreeAwarded;
  private final Instant registrationDate;
  private final boolean active;
  
  private Career(CareerID careerID, FacultyID facultyId, String trim, String description, int semesterLength,
    DegreeTitle degreeAwarded, boolean active, Instant now) {
    this.id = careerID;
    this.facultyId = facultyId;
    this.name = trim;
    this.description = description;
    this.semesterLength = semesterLength;
    this.degreeAwarded = degreeAwarded;
    this.registrationDate = now;
    this.active = active;
  }
  
  public static Career create(FacultyID facultyId, String name, String description,
    int semesterLength, DegreeTitle degreeAwarded, Instant now) {
    Objects.requireNonNull(now, "now no puede ser null");
    validateRequired(facultyId, name, semesterLength, degreeAwarded);
    return new Career(null, facultyId, trim(name), trimOrNull(description), semesterLength, degreeAwarded,
      true, now);
  }
  
  private static void validateRequired(FacultyID facultyId, String name, int semesterLength,
    DegreeTitle degree) {
    if (facultyId == null) {
      throw new FacultyRequiredException("facultyId es obligatorio.");
    }
    if (isBlank(name)) {
      throw new CareerNameRequiredException("name es obligatorio.");
    }
    if (semesterLength < 1 || semesterLength > 20) {
      throw new InvalidSemesterLengthException("semesterLength debe estar entre 1 y 20.");
    }
    if (degree == null) {
      throw new InvalidDegreeTitleException("degreeAwarded es obligatorio.");
    }
  }
  
  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }
  
  private static String trim(String s) {
    return s.trim();
  }
  
  private static String trimOrNull(String s) {
    return s == null ? null : s.trim();
  }
  
  public static Career rehydrate(CareerID careerID, FacultyID facultyID, String name,
    String description, Integer semesterLength, DegreeTitle degreeTitle, Instant registrationDate,
    Boolean active) {
    return new Career(careerID, facultyID, name, description, semesterLength, degreeTitle, active,
      registrationDate);
  }
  
  
  public CareerID getId() {
    return id;
  }
  
  public FacultyID getFacultyId() {
    return facultyId;
  }
  
  public String getName() {
    return name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public int getSemesterLength() {
    return semesterLength;
  }
  
  public DegreeTitle getDegreeAwarded() {
    return degreeAwarded;
  }
  
  public Instant getRegistrationDate() {
    return registrationDate;
  }
  
  public boolean isActive() {
    return active;
  }
}
