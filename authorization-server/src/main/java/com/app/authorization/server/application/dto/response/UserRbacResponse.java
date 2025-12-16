package com.app.authorization.server.application.dto.response;

import java.util.Set;

/**
 * @author Alonso
 */
public class UserRbacResponse {
  
  private Integer id;
  private String username;
  private String email;
  private String fullName;
  private Boolean twoFactorEnabled;
  private Boolean hasPassword;
  private Set<String> permissions;
  private Set<UIViewResponse> uiViews;
  
  public UserRbacResponse(Integer id, String username, String email, String fullName,
    Boolean twoFactorEnabled, Boolean hasPassword, Set<String> permissions,
    Set<UIViewResponse> uiViews) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.fullName = fullName;
    this.twoFactorEnabled = twoFactorEnabled;
    this.hasPassword = hasPassword;
    this.permissions = permissions;
    this.uiViews = uiViews;
  }
  
  public UserRbacResponse() {
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
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getFullName() {
    return fullName;
  }
  
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
  
  public Boolean getTwoFactorEnabled() {
    return twoFactorEnabled;
  }
  
  public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
    this.twoFactorEnabled = twoFactorEnabled;
  }
  
  public Boolean getHasPassword() {
    return hasPassword;
  }
  
  public void setHasPassword(Boolean hasPassword) {
    this.hasPassword = hasPassword;
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
