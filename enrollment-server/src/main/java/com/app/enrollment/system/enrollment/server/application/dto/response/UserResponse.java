package com.app.enrollment.system.enrollment.server.application.dto.response;

/**
 * @author Alonso
 */
public class UserResponse {
  
  private Integer id;
  private String username;
  private String fullName;
  
  public UserResponse() {
  }
  
  public UserResponse(Integer id, String username, String fullName) {
    this.id = id;
    this.username = username;
    this.fullName = fullName;
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getFullName() {
    return fullName;
  }
  
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
}
