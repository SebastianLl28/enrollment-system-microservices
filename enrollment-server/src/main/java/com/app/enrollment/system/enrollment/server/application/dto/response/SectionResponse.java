package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.time.Instant;

/**
 * @author Alonso
 */
public class SectionResponse {

  private Integer id;
  private CourseSummaryResponse course;
  private TermResponse term;
  private ClassroomResponse classroom;
  private String sectionCode;
  private Boolean active;
  private Instant createdAt;

  public SectionResponse() {
  }

  public SectionResponse(Integer id, CourseSummaryResponse course, TermResponse term,
    ClassroomResponse classroom, String sectionCode, Boolean active, Instant createdAt) {
    this.id = id;
    this.course = course;
    this.term = term;
    this.classroom = classroom;
    this.sectionCode = sectionCode;
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

  public void setCourse(CourseSummaryResponse course) {
    this.course = course;
  }

  public TermResponse getTerm() {
    return term;
  }

  public void setTerm(TermResponse term) {
    this.term = term;
  }

  public ClassroomResponse getClassroom() {
    return classroom;
  }

  public void setClassroom(ClassroomResponse classroom) {
    this.classroom = classroom;
  }

  public String getSectionCode() {
    return sectionCode;
  }

  public void setSectionCode(String sectionCode) {
    this.sectionCode = sectionCode;
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
