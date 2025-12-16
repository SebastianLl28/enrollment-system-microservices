package com.app.authorization.server.domain.model;

import com.app.authorization.server.domain.exception.InvalidFullNameException;
import com.app.authorization.server.domain.exception.InvalidUsernameException;
import com.app.authorization.server.domain.exception.TwoFactorNotInitializedException;
import com.app.authorization.server.domain.model.valueobject.Password;
import com.app.authorization.server.domain.model.valueobject.UserID;

/**
 * @author Alonso
 */
public class User {
  
  private UserID id;
  
  private String username;
  
  private Password password;
  
  private String fullName;
  
  private String email;
  
  private Boolean twoFactorEnabled;
  
  private String twoFactorSecret;
  
  private User() {
  }
  
  private Boolean validateUsername(String username) {
    return username != null && !username.trim().isEmpty();
  }
  
  private Boolean validateFullName(String fullName) {
    return fullName != null && !fullName.trim().isEmpty();
  }
  
  public static User create(String username, Password password, String fullName, String email) {
    validateRequiredFields(username, fullName);
    User user = new User();
    user.username = username;
    user.password = password;
    user.fullName = fullName;
    user.email = email;
    return user;
  }
  
  public static User create(String username, String fullName, String email) {
    validateRequiredFields(username, fullName);
    User user = new User();
    user.username = username;
    user.fullName = fullName;
    user.email = email;
    return user;
  }
  
  private static void validateRequiredFields
    (String username, String fullName) {
    if (username == null || username.trim().isEmpty()) {
      throw new InvalidUsernameException("Username cannot be null or empty");
    }
    if (fullName == null || fullName.trim().isEmpty()) {
      throw new InvalidFullNameException("Full name cannot be null or empty");
    }
  }
  
  public static User rehydrate(UserID userID, String username, Password password, String fullName, String email, Boolean twoFactorEnabled, String twoFactorSecret) {
    User user = new User();
    user.id = userID;
    user.username = username;
    user.password = password;
    user.fullName = fullName;
    user.email = email;
    user.twoFactorEnabled = twoFactorEnabled;
    user.twoFactorSecret = twoFactorSecret;
    return user;
  }
  
  public void initTwoFactor(String secret) {
    this.twoFactorSecret = secret;
    this.twoFactorEnabled = Boolean.FALSE;
  }
  
  public void enableTwoFactor() {
    if (this.twoFactorSecret == null) {
      throw new TwoFactorNotInitializedException("Two-factor secret not initialized");
    }
    this.twoFactorEnabled = Boolean.TRUE;
  }
  
  public void disableTwoFactor() {
    this.twoFactorSecret = null;
    this.twoFactorEnabled = Boolean.FALSE;
  }
  
  public Boolean requiresTwoFactorAuth() {
    return Boolean.TRUE.equals(twoFactorEnabled) && twoFactorSecret != null;
  }
  
  public String getUsername() {
    return username;
  }

  public Password getPassword() {
    return password;
  }
  
  public String getFullName() {
    return fullName;
  }
  
  public String getEmail() {
    return email;
  }
  
  public UserID getId() {
    return id;
  }
  
  public Boolean getTwoFactorEnabled() {
    return twoFactorEnabled;
  }
  
  public String getTwoFactorSecret() {
    return twoFactorSecret;
  }
  
  public Boolean hasPassword() {
    return this.password != null && this.password.getValue() != null && !this.password.getValue().isEmpty();
  }
}

