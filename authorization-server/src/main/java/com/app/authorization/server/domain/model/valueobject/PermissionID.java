package com.app.authorization.server.domain.model.valueobject;

/**
 * @author Alonso
 */
public class PermissionID {
  
  private final Integer value;
  
  public PermissionID(Integer value) {
    this.value = value;
  }
  
  public Integer getValue() {
    return value;
  }

}
