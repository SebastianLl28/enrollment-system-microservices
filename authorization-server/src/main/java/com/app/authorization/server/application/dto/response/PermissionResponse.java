package com.app.authorization.server.application.dto.response;

import com.app.authorization.server.domain.model.enums.OperationType;
import com.app.authorization.server.domain.model.enums.ResourceType;
import com.app.authorization.server.domain.model.enums.ScopeType;

/**
 * @author Alonso
 */
public class PermissionResponse {
  
  private Integer id;
  private ResourceType resource;
  private OperationType operation;
  private ScopeType scope;
  private String description;
  
  public PermissionResponse(Integer id, ResourceType resource, OperationType operation,
    ScopeType scope, String description) {
    this.id = id;
    this.resource = resource;
    this.operation = operation;
    this.scope = scope;
    this.description = description;
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public ResourceType getResource() {
    return resource;
  }
  
  public void setResource(ResourceType resource) {
    this.resource = resource;
  }
  
  public OperationType getOperation() {
    return operation;
  }
  
  public void setOperation(OperationType operation) {
    this.operation = operation;
  }
  
  public ScopeType getScope() {
    return scope;
  }
  
  public void setScope(ScopeType scope) {
    this.scope = scope;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
}
