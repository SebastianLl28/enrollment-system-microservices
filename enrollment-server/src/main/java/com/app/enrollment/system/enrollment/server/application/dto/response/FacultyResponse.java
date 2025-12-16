package com.app.enrollment.system.enrollment.server.application.dto.response;

/**
 * @author Alonso
 */
public class FacultyResponse {

  private Integer id;
  private String name;
  private String location;
  private Boolean active;
  private String dean;

  public FacultyResponse() {
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

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
  
  public String getDean() {
    return dean;
  }
  
  public void setDean(String dean) {
    this.dean = dean;
  }
}
