package com.app.enrollment.system.enrollment.server.application.dto.response;

/**
 * @author Alonso
 */
public class CourseSummaryResponse {

  private Integer id;
  private String name;
  private String code;
  private Boolean active;

  public CourseSummaryResponse() {
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
}
