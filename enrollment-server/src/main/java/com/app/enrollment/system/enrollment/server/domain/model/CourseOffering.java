package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Alonso
 */
public class CourseOffering {

  private final CourseOfferingID id;
  private final CourseID courseId;
  private final TermID termId;
  private final String sectionCode; // "A", "B"
  private final Integer capacity;
  private Integer enrolledCount;
  private final Boolean active;
  private final Instant createdAt;
  private final BigDecimal price; // precio de la inscripción para este curso-periodo

  private CourseOffering(CourseOfferingID id, CourseID courseId, TermID termId, String sectionCode,
    Integer capacity, Integer enrolledCount, Boolean active, Instant createdAt, BigDecimal price) {
    this.id = id;
    this.courseId = courseId;
    this.termId = termId;
    this.sectionCode = sectionCode;
    this.capacity = capacity;
    this.enrolledCount = enrolledCount;
    this.active = active;
    this.createdAt = createdAt;
    this.price = price;
  }

  public static CourseOffering create(CourseID courseId, TermID termId, String sectionCode,
    Integer capacity, BigDecimal price, Instant now) {
    return new CourseOffering(null, courseId, termId, sectionCode, capacity, 0, true, now, price);
  }

  public static CourseOffering rehydrate(CourseOfferingID id, CourseID courseId, TermID termId,
    String sectionCode, Integer capacity, Integer enrolledCount, Boolean active,
    Instant createdAt, BigDecimal price) {
    return new CourseOffering(id, courseId, termId, sectionCode, capacity, enrolledCount, active,
      createdAt, price);
  }
  
  public void incrementEnrolledCount() {
    if (enrolledCount == null) {
      enrolledCount = 0;
    }
    if (enrolledCount < capacity) {
      enrolledCount++;
    } else {
      throw new IllegalStateException("Cannot enroll more students than the capacity.");
    }
  }
  
  
  public void decrementEnrolledCount() {
    if (enrolledCount == null) {
      enrolledCount = 0;
    }
    if (enrolledCount > 0) {
      enrolledCount--;
    } else {
      throw new IllegalStateException("Enrolled count cannot be negative.");
    }
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
  
  public Integer getEnrolledCount() {
    return enrolledCount;
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

  public BigDecimal getPrice() {
    return price;
  }
}
