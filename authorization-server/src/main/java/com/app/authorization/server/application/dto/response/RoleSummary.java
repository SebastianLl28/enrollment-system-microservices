package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */
public class RoleSummary {
  private Integer id;
  private String name;
  
  public RoleSummary(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
  
  public Integer getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
