package com.app.authorization.server.infrastructure.adapter.out.persistence.mapper;

import com.app.authorization.server.domain.model.Role;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import com.app.authorization.server.domain.model.valueobject.RoleID;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.PermissionJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.RoleJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UIViewJpaEntity;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class RoleJpaMapper {
  
  public RoleJpaEntity toEntity(
    Role role,
    List<PermissionJpaEntity> permissions,
    List<UIViewJpaEntity> views
  ) {
    Integer idValue = role.getId() != null ? role.getId().getValue() : null;
    
    RoleJpaEntity entity = new RoleJpaEntity(idValue, role.getName(), role.getDescription());
    
    // Permisos
    entity.getPermissions().clear();
    if (permissions != null && !permissions.isEmpty()) {
      entity.getPermissions().addAll(permissions);
    }
    
    // Vistas
    entity.getViews().clear();
    if (views != null && !views.isEmpty()) {
      entity.getViews().addAll(views);
    }
    
    return entity;
  }
  
  public Role toDomain(RoleJpaEntity entity) {
    RoleID id = new RoleID(entity.getId());
    
    Set<PermissionID> permissionIds = entity.getPermissions().stream()
      .map(p -> new PermissionID(p.getId()))
      .collect(Collectors.toSet());
    
    Set<String> viewCodes = entity.getViews().stream()
      .map(UIViewJpaEntity::getCode)
      .collect(Collectors.toSet());
    
    return Role.rehydrate(
      id,
      entity.getName(),
      entity.getDescription(),
      permissionIds,
      viewCodes
    );
  }
}
