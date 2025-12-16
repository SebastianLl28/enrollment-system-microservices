package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */
import java.util.List;

public class UserRolesResponse {
  
  private Integer userId;
  private String username;
  private List<RoleSummary> roles; // roles asignados
  
  public UserRolesResponse(Integer userId, String username, List<RoleSummary> roles) {
    this.userId = userId;
    this.username = username;
    this.roles = roles;
  }
  
  public Integer getUserId() {
    return userId;
  }
  
  public String getUsername() {
    return username;
  }
  
  public List<RoleSummary> getRoles() {
    return roles;
  }
  
  public void setUserId(Integer userId) {
    this.userId = userId;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public void setRoles(
    List<RoleSummary> roles) {
    this.roles = roles;
  }
}
