package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerCourseJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface CareerCourseJpaRepository extends JpaRepository<CareerCourseJpaEntity, Integer> {

  List<CareerCourseJpaEntity> findByCourseId(Integer courseId);

  List<CareerCourseJpaEntity> findByCareerId(Integer careerId);

  void deleteByCourseId(Integer courseId);
}
