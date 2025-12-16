package com.app.authorization.server.domain.model.valueobject;

public class UserID {
  private final Integer value;
  
  public UserID(Integer value) {
    this.value = value;
  }
 
  public Integer getValue() {
    return value;
  }
}
