package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.util.List;

public class CourseResponse {

  private Integer id;
  private String code;
  private String name;
  private String description;
  private boolean active;
  private int credits;
  private int semesterLevel;
  private List<StudentSummaryResponse> enrolledStudentList;

  public CourseResponse() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<StudentSummaryResponse> getEnrolledStudentList() {
    return enrolledStudentList;
  }

  public void setEnrolledStudentList(List<StudentSummaryResponse> enrolledStudentList) {
    this.enrolledStudentList = enrolledStudentList;
  }
  
  public int getCredits() {
    return credits;
  }
  
  public void setCredits(int credits) {
    this.credits = credits;
  }
  
  public int getSemesterLevel() {
    return semesterLevel;
  }
  
  public void setSemesterLevel(int semesterLevel) {
    this.semesterLevel = semesterLevel;
  }
}
