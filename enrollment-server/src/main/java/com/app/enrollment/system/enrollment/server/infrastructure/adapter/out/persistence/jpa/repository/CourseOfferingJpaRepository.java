package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CourseOfferingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface CourseOfferingJpaRepository extends JpaRepository<CourseOfferingJpaEntity, Integer> {

}
