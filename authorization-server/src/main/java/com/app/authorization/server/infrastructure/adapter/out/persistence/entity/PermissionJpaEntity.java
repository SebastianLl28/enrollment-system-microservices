package com.app.authorization.server.infrastructure.adapter.out.persistence.entity;

import com.app.authorization.server.domain.model.enums.OperationType;
import com.app.authorization.server.domain.model.enums.ResourceType;
import com.app.authorization.server.domain.model.enums.ScopeType;
import jakarta.persistence.*;

/**
 * @author Alonso
 */
@Entity
@Table(name = "permission")
public class PermissionJpaEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private ResourceType resource;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private OperationType operation;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private ScopeType scope;
  
  @Column(length = 255)
  private String description;
  
  protected PermissionJpaEntity() {
  }
  
  public PermissionJpaEntity(Integer id, ResourceType resource, OperationType operation,
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
