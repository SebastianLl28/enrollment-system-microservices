package com.app.authorization.server.infrastructure.adapter.out.persistence.mapper;

import com.app.authorization.server.domain.model.Permission;
import com.app.authorization.server.domain.model.enums.OperationType;
import com.app.authorization.server.domain.model.enums.ResourceType;
import com.app.authorization.server.domain.model.enums.ScopeType;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.PermissionJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class PermissionJpaMapper {
  
  public PermissionJpaEntity toJpaEntity(Permission permission) {
    Integer idValue = permission.getId() != null ? permission.getId().getValue() : null;
    return new PermissionJpaEntity(
      idValue,
      permission.getResource(),
      permission.getOperation(),
      permission.getScope(),
      permission.getDescription()
    );
  }
  
  public Permission toDomain(PermissionJpaEntity entity) {
    PermissionID id = new PermissionID(entity.getId());
    ResourceType resource = entity.getResource();
    OperationType operation = entity.getOperation();
    ScopeType scope = entity.getScope();
    String description = entity.getDescription();
    
    return Permission.rehydrate(id, resource, operation, scope, description);
    
  }
  
}
