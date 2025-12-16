package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */
public class TwoFactorConfirmResponse {
  
  private boolean twoFactorEnabled;
  
  public TwoFactorConfirmResponse(boolean twoFactorEnabled) {
    this.twoFactorEnabled = twoFactorEnabled;
  }
  
  public boolean isTwoFactorEnabled() {
    return twoFactorEnabled;
  }
  
  public void setTwoFactorEnabled(boolean twoFactorEnabled) {
    this.twoFactorEnabled = twoFactorEnabled;
  }
  
}
