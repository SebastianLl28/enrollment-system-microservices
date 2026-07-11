package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerOfferingJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface CareerOfferingJpaRepository extends JpaRepository<CareerOfferingJpaEntity, Integer> {

  long countByActiveTrue();

  List<CareerOfferingJpaEntity> findByTermId(Integer termId);
}
