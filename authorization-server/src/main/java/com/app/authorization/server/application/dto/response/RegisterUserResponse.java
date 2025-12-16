package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */
public class RegisterUserResponse {
  
  private String username;
  private String message = "Usuario registrado con éxito";
  
  public RegisterUserResponse(String username) {
    this.username = username;
  }
  
  public RegisterUserResponse() {
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
}
