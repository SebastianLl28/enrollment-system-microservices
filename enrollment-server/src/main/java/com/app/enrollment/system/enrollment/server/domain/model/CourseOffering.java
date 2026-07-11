package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import java.time.Instant;

/**
 * Sección de un curso dictada en un periodo (catálogo/horarios). La matrícula
 * y el pago se hacen contra {@link CareerOffering}, no contra esta entidad.
 *
 * @author Alonso
 */
public class CourseOffering {

  private final CourseOfferingID id;
  private final CourseID courseId;
  private final TermID termId;
  private final String sectionCode; // "A", "B"
  private final Integer capacity;
  private final Boolean active;
  private final Instant createdAt;

  private CourseOffering(CourseOfferingID id, CourseID courseId, TermID termId, String sectionCode,
    Integer capacity, Boolean active, Instant createdAt) {
    this.id = id;
    this.courseId = courseId;
    this.termId = termId;
    this.sectionCode = sectionCode;
    this.capacity = capacity;
    this.active = active;
    this.createdAt = createdAt;
  }

  public static CourseOffering create(CourseID courseId, TermID termId, String sectionCode,
    Integer capacity, Instant now) {
    return new CourseOffering(null, courseId, termId, sectionCode, capacity, true, now);
  }

  public static CourseOffering rehydrate(CourseOfferingID id, CourseID courseId, TermID termId,
    String sectionCode, Integer capacity, Boolean active, Instant createdAt) {
    return new CourseOffering(id, courseId, termId, sectionCode, capacity, active, createdAt);
  }

  public CourseOfferingID getId() {
    return id;
  }

  public CourseID getCourseId() {
    return courseId;
  }

  public TermID getTermId() {
    return termId;
  }

  public String getSectionCode() {
    return sectionCode;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public Boolean isActive() {
    return active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Boolean getActive() {
    return active;
  }
}
