package com.app.enrollment.system.enrollment.server.application.dto.response;

/**
 * @author Alonso
 */
public class FacultySummaryResponse {

  private Integer id;
  private String name;

  public FacultySummaryResponse() {
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
}
