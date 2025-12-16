package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.TermJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface TermJpaRepository extends JpaRepository<TermJpaEntity, Integer> {
  
  Optional<TermJpaEntity> findByCode(String code);
  
}
