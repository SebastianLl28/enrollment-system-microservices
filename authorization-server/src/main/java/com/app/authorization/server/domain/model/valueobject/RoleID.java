package com.app.authorization.server.domain.model.valueobject;

/**
 * @author Alonso
 */
public class RoleID {
  
  private final Integer id;
  
  public RoleID(Integer id) {
    this.id = id;
  }
  
  public Integer getId() {
    return id;
  }
  
  public Integer getValue() {
    return id;
  }
}
