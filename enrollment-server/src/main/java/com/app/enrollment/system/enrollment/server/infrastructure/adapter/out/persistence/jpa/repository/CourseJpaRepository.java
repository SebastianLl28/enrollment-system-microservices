package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CourseJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alonso
 */
public interface CourseJpaRepository extends JpaRepository<CourseJpaEntity, Integer> {

  @Query("SELECT c FROM CourseJpaEntity c WHERE c.careerId = :careerId AND c.active = true")
  List<CourseJpaEntity> findAllByCareerIdAndActiveIsFalse(Integer careerId);
}
