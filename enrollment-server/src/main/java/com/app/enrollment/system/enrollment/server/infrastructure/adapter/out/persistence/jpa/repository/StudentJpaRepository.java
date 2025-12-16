package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.StudentJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alonso
 */
public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, Integer> {

  @Query("""
    SELECT s
    FROM StudentJpaEntity s
    WHERE s.studentId IN (
      SELECT sc.studentId
      FROM EnrollmentJpaEntity sc
    WHERE sc.status in ('PAID', 'COMPLETED') AND sc.courseOffering.courseId = :courseId
    )
  """)
  List<StudentJpaEntity> findAllByCourseId(Integer courseId);
}
