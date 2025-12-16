package com.app.authorization.server.infrastructure.adapter.out.persistence.repository;

import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.RoleJpaEntity;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface JpaRoleRepository extends JpaRepository<RoleJpaEntity, Integer> {

  Set<RoleJpaEntity> findAllByIdIn(Collection<Integer> ids);
}
