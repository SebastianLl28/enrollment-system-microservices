package com.app.authorization.server.application.dto.response;

import java.util.Set;

/**
 * @author Alonso
 */
public class ValidateTokenResponse {
  private String username;
  private boolean valid;
  private Integer userId;
  private Set<String> permissions;
  private Set<UIViewResponse> uiViews;
  
  public ValidateTokenResponse() {
  }
  
  public ValidateTokenResponse(String username, boolean valid, Integer userId,
    Set<String> permissions, Set<UIViewResponse> uiViews) {
    this.username = username;
    this.valid = valid;
    this.userId = userId;
    this.permissions = permissions;
    this.uiViews = uiViews;
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
  
  public Set<UIViewResponse> getUiViews() {
    return uiViews;
  }
  
  public void setUiViews(
    Set<UIViewResponse> uiViews) {
    this.uiViews = uiViews;
  }
}
