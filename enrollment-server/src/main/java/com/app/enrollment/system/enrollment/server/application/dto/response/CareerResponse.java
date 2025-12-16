package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.time.Instant;
import java.util.List;

public class CareerResponse {

  private Integer id;
  private FacultySummaryResponse faculty;
  private String name;
  private String description;
  private Integer semesterLength;
  private String degreeAwarded;
  private Instant registrationDate;
  private boolean active;
  private List<CourseSummaryResponse> courseList;

  public CareerResponse() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public FacultySummaryResponse getFaculty() {
    return faculty;
  }

  public void setFaculty(FacultySummaryResponse faculty) {
    this.faculty = faculty;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getSemesterLength() {
    return semesterLength;
  }

  public void setSemesterLength(Integer semesterLength) {
    this.semesterLength = semesterLength;
  }

  public String getDegreeAwarded() {
    return degreeAwarded;
  }

  public void setDegreeAwarded(String degreeAwarded) {
    this.degreeAwarded = degreeAwarded;
  }

  public Instant getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Instant registrationDate) {
    this.registrationDate = registrationDate;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<CourseSummaryResponse> getCourseList() {
    return courseList;
  }

  public void setCourseList(List<CourseSummaryResponse> courseList) {
    this.courseList = courseList;
  }
}
