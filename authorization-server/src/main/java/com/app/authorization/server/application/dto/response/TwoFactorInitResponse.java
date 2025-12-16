package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */
public class TwoFactorInitResponse {
  
  private String secret;
  private String otpauthUrl;
  
  public TwoFactorInitResponse() {
  }
  
  public TwoFactorInitResponse(String secret, String otpauthUrl) {
    this.secret = secret;
    this.otpauthUrl = otpauthUrl;
  }
  
  public String getSecret() {
    return secret;
  }
  
  public void setSecret(String secret) {
    this.secret = secret;
  }
  
  public String getOtpauthUrl() {
    return otpauthUrl;
  }
  
  public void setOtpauthUrl(String otpauthUrl) {
    this.otpauthUrl = otpauthUrl;
  }
}
