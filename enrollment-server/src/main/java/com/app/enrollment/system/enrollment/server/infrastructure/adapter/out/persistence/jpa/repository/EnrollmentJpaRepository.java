package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.EnrollmentJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Alonso
 */
public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentJpaEntity, Integer> {
  
  @Query("""
    SELECT e FROM EnrollmentJpaEntity e
    WHERE e.studentId = :studentId
      AND e.courseOfferingId = :courseOfferingId
      AND e.status = :status
     ORDER BY e.createdAt ASC
    """)
  Optional<EnrollmentJpaEntity> findByStudentIdAndCourseOfferingIdAndStatus(Integer studentId,
    Integer courseOfferingId, EnrollmentStatus status);
  
  @Query("""
      SELECT e
      FROM EnrollmentJpaEntity e
      JOIN CourseOfferingJpaEntity co ON e.courseOfferingId = co.id
      WHERE (:studentId IS NULL OR e.studentId = :studentId)
        AND (:termId   IS NULL OR co.termId   = :termId)
        AND (:courseId IS NULL OR co.courseId = :courseId)
      ORDER BY e.createdAt ASC
    """)
  List<EnrollmentJpaEntity> findAllByStudentIDAndTermIDAndCourseID(@Param("studentId") Integer studentId,
    @Param("termId") Integer termId, @Param("courseId") Integer courseId);
}
