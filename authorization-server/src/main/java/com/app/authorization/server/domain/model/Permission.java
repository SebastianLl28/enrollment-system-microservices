package com.app.authorization.server.domain.model;

import com.app.authorization.server.domain.exception.InvalidOperationException;
import com.app.authorization.server.domain.exception.InvalidResourceException;
import com.app.authorization.server.domain.model.enums.OperationType;
import com.app.authorization.server.domain.model.enums.ResourceType;
import com.app.authorization.server.domain.model.enums.ScopeType;
import com.app.authorization.server.domain.model.valueobject.PermissionID;

/**
 * @author Alonso
 */
public final class Permission {
  
  private PermissionID id;
  
  private ResourceType resource;
  
  private OperationType operation;
  
  private ScopeType scope;
  
  private String description;
  
  private Permission() {
  }
  
  private static void validateRequiredFields(ResourceType resource, OperationType operation) {
    if (resource == null) {
      throw new InvalidResourceException("Resource cannot be null or empty");
    }
    if (operation == null) {
      throw new InvalidOperationException("Operation cannot be null or empty");
    }
  }
  
  public static Permission create(ResourceType resource, OperationType operation, ScopeType scope,
    String description) {
    validateRequiredFields(resource, operation);
    Permission permission = new Permission();
    permission.resource = resource;
    permission.operation = operation;
    permission.scope = scope;
    permission.description = description;
    return permission;
  }
  
  public static Permission rehydrate(PermissionID id, ResourceType resource, OperationType operation,
    ScopeType scope, String description) {
    Permission permission = new Permission();
    permission.id = id;
    permission.resource = resource;
    permission.operation = operation;
    permission.scope = scope;
    permission.description = description;
    return permission;
  }
  
  public boolean matches(ResourceType resource, OperationType operation, ScopeType scope) {
    if (!this.resource.equals(resource)) return false;
    if (!this.operation.equals(operation)) return false;
    if (this.scope == null) return scope == null;
    return this.scope.equals(scope);
  }
  
  public PermissionID getId() {
    return id;
  }
  
  public ResourceType getResource() {
    return resource;
  }
  
  public OperationType getOperation() {
    return operation;
  }
  
  public ScopeType getScope() {
    return scope;
  }
  
  public String getDescription() {
    return description;
  }
}
