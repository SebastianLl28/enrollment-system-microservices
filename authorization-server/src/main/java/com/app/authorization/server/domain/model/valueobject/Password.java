package com.app.authorization.server.domain.model.valueobject;

import com.app.authorization.server.domain.exception.InvalidPasswordException;
import com.app.authorization.server.domain.repository.PasswordHasher;

/**
 * @author Alonso
 */
public class Password {
  
  private final String value;
  private static final int MIN_LENGTH = 6;
  
  private Password(String value) {
    this.value = value;
  }
  
  public static Password fromHash(String hash) {
    if (hash == null || hash.isBlank()) {
      throw new InvalidPasswordException("Password hash cannot be null or blank");
    }
    return new Password(hash);
  }
  
  public static Password fromRaw(String rawPassword, PasswordHasher hasher) {
    if (rawPassword == null || rawPassword.length() < MIN_LENGTH) {
      throw new InvalidPasswordException(MIN_LENGTH);
    }
    String hash = hasher.hash(rawPassword);
    return new Password(hash);
  }
  
  public boolean matches(String rawPassword, PasswordHasher hasher) {
    return hasher.verify(rawPassword, this.value);
  }
  
  public String getValue() {
    return value;
  }
}
