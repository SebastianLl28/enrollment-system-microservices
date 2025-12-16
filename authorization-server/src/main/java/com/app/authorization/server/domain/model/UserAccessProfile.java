package com.app.authorization.server.domain.model;

import java.util.Set;

/**
 * @author Alonso
 */
public final class UserAccessProfile {
  
  private final Integer userId;
  private final String username;
  private final Set<String> roles;
  private final Set<String> permissions;
  private final Set<String> viewCodes;
  
  public UserAccessProfile(Integer userId,
    String username,
    Set<String> roles,
    Set<String> permissions,
    Set<String> viewCodes) {
    this.userId = userId;
    this.username = username;
    this.roles = roles;
    this.permissions = permissions;
    this.viewCodes = viewCodes;
  }
  
  public Integer getUserId() {
    return userId;
  }
  
  public String getUsername() {
    return username;
  }
  
  public Set<String> getRoles() {
    return roles;
  }
  
  public Set<String> getPermissions() {
    return permissions;
  }
  
  public Set<String> getViewCodes() {
    return viewCodes;
  }
}
