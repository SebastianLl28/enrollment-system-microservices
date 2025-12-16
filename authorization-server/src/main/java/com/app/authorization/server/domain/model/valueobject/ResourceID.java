package com.app.authorization.server.domain.model.valueobject;

/**
 * @author Alonso
 */
public class ResourceID {
  
  private final Integer value;
  
  private ResourceID(Integer value) {
    this.value = value;
  }
  
  public Integer getValue() {
    return value;
  }
  
}
