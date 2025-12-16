package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */
public class VerifyTwoFactorResponse {
  
  private String accessToken;
  private String username;
  
  public VerifyTwoFactorResponse() {
  }
  
  public VerifyTwoFactorResponse(String accessToken, String username) {
    this.accessToken = accessToken;
    this.username = username;
  }
  
  public String getAccessToken() {
    return accessToken;
  }
  
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
}
