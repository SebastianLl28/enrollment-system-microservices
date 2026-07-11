package com.app.enrollment.system.enrollment.server.application.dto.response;

/**
 * Carrera a la que pertenece un curso, con el ciclo en que se dicta en esa carrera.
 *
 * @author Alonso
 */
public class CareerAssignmentResponse {

  private Integer careerId;
  private String careerName;
  private Integer semesterLevel;

  public CareerAssignmentResponse() {
  }

  public CareerAssignmentResponse(Integer careerId, String careerName, Integer semesterLevel) {
    this.careerId = careerId;
    this.careerName = careerName;
    this.semesterLevel = semesterLevel;
  }

  public Integer getCareerId() {
    return careerId;
  }

  public void setCareerId(Integer careerId) {
    this.careerId = careerId;
  }

  public String getCareerName() {
    return careerName;
  }

  public void setCareerName(String careerName) {
    this.careerName = careerName;
  }

  public Integer getSemesterLevel() {
    return semesterLevel;
  }

  public void setSemesterLevel(Integer semesterLevel) {
    this.semesterLevel = semesterLevel;
  }
}
