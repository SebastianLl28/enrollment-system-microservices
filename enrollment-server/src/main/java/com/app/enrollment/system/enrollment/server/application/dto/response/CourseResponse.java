package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.util.List;

public class CourseResponse {

  private Integer id;
  private String code;
  private String name;
  private String description;
  private boolean active;
  private int credits;
  private List<CareerAssignmentResponse> careers;

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

  public int getCredits() {
    return credits;
  }

  public void setCredits(int credits) {
    this.credits = credits;
  }

  public List<CareerAssignmentResponse> getCareers() {
    return careers;
  }

  public void setCareers(List<CareerAssignmentResponse> careers) {
    this.careers = careers;
  }
}
