package com.app.enrollment.system.enrollment.server.application.dto.response;

/**
 * @author Alonso
 */
public class StudentSummaryResponse {

  private Integer id;
  private String firstName;
  private String lastName;

  public StudentSummaryResponse() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

}
