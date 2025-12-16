package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.OutboxEventJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.enums.OutboxStatusType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventJpaEntity, Integer> {
  
  List<OutboxEventJpaEntity> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatusType status);
  
}
