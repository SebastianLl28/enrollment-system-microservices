package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.time.Instant;

/**
 * @author Alonso
 */
public class CourseOfferingResponse {
  
  private Integer id;
  private CourseSummaryResponse course;
  private TermResponse term;
  private String section;
  private Integer capacity;
  private Integer enrolledCount;
  private Boolean active;
  private Instant createdAt;
  
  public CourseOfferingResponse() {
  }
  
  public CourseOfferingResponse(Integer id, CourseSummaryResponse course, TermResponse term,
    String section, Integer capacity, Integer enrolledCount, Boolean active, Instant createdAt) {
    this.id = id;
    this.course = course;
    this.term = term;
    this.section = section;
    this.capacity = capacity;
    this.enrolledCount = enrolledCount;
    this.active = active;
    this.createdAt = createdAt;
  }
  
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public CourseSummaryResponse getCourse() {
    return course;
  }
  
  public void setCourse(
    CourseSummaryResponse course) {
    this.course = course;
  }
  
  public TermResponse getTerm() {
    return term;
  }
  
  public void setTerm(
    TermResponse term) {
    this.term = term;
  }
  
  public String getSection() {
    return section;
  }
  
  public void setSection(String section) {
    this.section = section;
  }
  
  public Integer getCapacity() {
    return capacity;
  }
  
  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }
  
  public Integer getEnrolledCount() {
    return enrolledCount;
  }
  
  public void setEnrolledCount(Integer enrolledCount) {
    this.enrolledCount = enrolledCount;
  }
  
  public Boolean getActive() {
    return active;
  }
  
  public void setActive(Boolean active) {
    this.active = active;
  }
  
  public Instant getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
