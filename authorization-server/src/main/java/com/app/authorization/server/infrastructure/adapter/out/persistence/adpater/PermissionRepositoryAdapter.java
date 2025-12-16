package com.app.authorization.server.infrastructure.adapter.out.persistence.adpater;

import com.app.authorization.server.domain.model.Permission;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import com.app.authorization.server.domain.repository.PermissionRepository;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.PermissionJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.mapper.PermissionJpaMapper;
import com.app.authorization.server.infrastructure.adapter.out.persistence.repository.JpaPermissionRepository;
import com.app.common.annotation.Adapter;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class PermissionRepositoryAdapter implements PermissionRepository {
  
  private final JpaPermissionRepository jpaRepository;
  private final PermissionJpaMapper mapper;
  
  public PermissionRepositoryAdapter(JpaPermissionRepository jpaRepository, PermissionJpaMapper mapper) {
    this.jpaRepository = jpaRepository;
    this.mapper = mapper;
  }
  
  @Override
  public Permission save(Permission permission) {
    PermissionJpaEntity entity = mapper.toJpaEntity(permission);
    PermissionJpaEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }
  
  @Override
  public Optional<Permission> findById(PermissionID id) {
    return jpaRepository.findById(id.getValue())
      .map(mapper::toDomain);
  }
  
  @Override
  public List<Permission> findAll() {
    return jpaRepository.findAll().stream()
      .map(mapper::toDomain).toList();
  }
  
  @Override
  public void deleteById(PermissionID id) {
    jpaRepository.deleteById(id.getValue());
  }
}
