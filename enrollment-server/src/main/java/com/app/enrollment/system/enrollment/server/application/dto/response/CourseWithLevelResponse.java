package com.app.enrollment.system.enrollment.server.application.dto.response;

/**
 * Curso dentro de la malla de una carrera, con el ciclo en que se dicta.
 *
 * @author Alonso
 */
public class CourseWithLevelResponse {

  private Integer id;
  private String name;
  private String code;
  private Boolean active;
  private Integer semesterLevel;

  public CourseWithLevelResponse() {
  }

  public CourseWithLevelResponse(Integer id, String name, String code, Boolean active,
    Integer semesterLevel) {
    this.id = id;
    this.name = name;
    this.code = code;
    this.active = active;
    this.semesterLevel = semesterLevel;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Integer getSemesterLevel() {
    return semesterLevel;
  }

  public void setSemesterLevel(Integer semesterLevel) {
    this.semesterLevel = semesterLevel;
  }
}
