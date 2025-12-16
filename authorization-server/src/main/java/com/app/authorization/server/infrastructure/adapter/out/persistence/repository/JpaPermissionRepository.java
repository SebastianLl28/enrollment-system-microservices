package com.app.authorization.server.infrastructure.adapter.out.persistence.repository;

import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.PermissionJpaEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface JpaPermissionRepository extends JpaRepository<PermissionJpaEntity, Integer> {
  
  List<PermissionJpaEntity> findByIdIn(Collection<Integer> ids);
}
