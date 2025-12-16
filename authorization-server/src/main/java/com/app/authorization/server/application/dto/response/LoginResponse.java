package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */
public class LoginResponse {
  
  private String token;
  
  private String username;
  
  private Boolean twoFactorRequired;
  
  private String message = "Inicio de sesión exitoso";
  
  public LoginResponse() {
  }
  
  public LoginResponse(String token, String username, Boolean twoFactorRequired) {
    this.token = token;
    this.username = username;
    this.twoFactorRequired = twoFactorRequired;
  }
  
  public String getToken() {
    return token;
  }
  
  public void setToken(String token) {
    this.token = token;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public Boolean getTwoFactorRequired() {
    return twoFactorRequired;
  }
  
  public void setTwoFactorRequired(Boolean twoFactorRequired) {
    this.twoFactorRequired = twoFactorRequired;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
}
