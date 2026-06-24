package com.app.api.gateway.dto;

import java.util.Set;

/**
 * @author Alonso
 */
public class ValidationResponse {
  
  private String username;
  private boolean valid;
  private Integer userId;
  private Set<String> permissions;
  
  public ValidationResponse() {
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public boolean isValid() {
    return valid;
  }
  
  public void setValid(boolean valid) {
    this.valid = valid;
  }
  
  public Integer getUserId() {
    return userId;
  }
  
  public void setUserId(Integer userId) {
    this.userId = userId;
  }
  
  public Set<String> getPermissions() {
    return permissions;
  }
  
  public void setPermissions(Set<String> permissions) {
    this.permissions = permissions;
  }
  
  @Override
  public String toString() {
    return "ValidationResponse{" +
        "username='" + username + '\'' +
        ", valid=" + valid +
        ", userId=" + userId +
        ", permissions=" + permissions +
        '}';
  }
}
