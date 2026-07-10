package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.TermJpaEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Alonso
 */
public interface TermJpaRepository extends JpaRepository<TermJpaEntity, Integer> {

  Optional<TermJpaEntity> findByCode(String code);

  @Query("""
    SELECT t FROM TermJpaEntity t
    WHERE t.active = true AND :today BETWEEN t.startDate AND t.endDate
    ORDER BY t.startDate DESC
    """)
  List<TermJpaEntity> findCurrentTerms(@Param("today") LocalDate today);

}
