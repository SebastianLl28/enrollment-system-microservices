package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */
public class UserResponse {
  
  private Integer id;
  private String username;
  private String email;
  private String fullName;
  private Boolean twoFactorEnabled;
  private Boolean hasPassword;
  
  public UserResponse() {
  }
  
  public UserResponse(Integer id, String username, String email, String fullName,
    Boolean twoFactorEnabled, Boolean hasPassword) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.fullName = fullName;
    this.twoFactorEnabled = twoFactorEnabled;
    this.hasPassword = hasPassword;
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
}
