package com.app.authorization.server.application.dto.response;

import java.util.List;

/**
 * @author Alonso
 */
public class MyViewResponse {
  
  private String username;
  private List<String> roles;
  private List<String> permissions;
  private List<UIViewResponse> views;
  
  public MyViewResponse(String username, List<String> roles,
    List<String> permissions, List<UIViewResponse> views) {
    this.username = username;
    this.roles = roles;
    this.permissions = permissions;
    this.views = views;
  }
  
  public String getUsername() {
    return username;
  }
  
  public List<String> getRoles() {
    return roles;
  }
  
  public List<String> getPermissions() {
    return permissions;
  }
  
  public List<UIViewResponse> getViews() {
    return views;
  }
}
