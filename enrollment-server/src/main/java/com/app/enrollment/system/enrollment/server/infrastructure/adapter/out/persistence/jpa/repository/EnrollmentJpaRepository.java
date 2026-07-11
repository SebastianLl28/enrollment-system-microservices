package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.EnrollmentJpaEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
      AND e.careerOfferingId = :careerOfferingId
      AND e.status IN :statuses
     ORDER BY e.createdAt ASC
    """)
  Optional<EnrollmentJpaEntity> findByStudentIdAndCareerOfferingIdAndStatusIn(
    @Param("studentId") Integer studentId,
    @Param("careerOfferingId") Integer careerOfferingId,
    @Param("statuses") Collection<EnrollmentStatus> statuses);

  @Query("""
      SELECT e
      FROM EnrollmentJpaEntity e
      JOIN CareerOfferingJpaEntity co ON e.careerOfferingId = co.id
      WHERE (:studentId IS NULL OR e.studentId = :studentId)
        AND (:termId   IS NULL OR co.termId   = :termId)
        AND (:careerId IS NULL OR co.careerId = :careerId)
      ORDER BY e.createdAt DESC
    """)
  Page<EnrollmentJpaEntity> findAllByStudentIDAndTermIDAndCareerID(@Param("studentId") Integer studentId,
    @Param("termId") Integer termId, @Param("careerId") Integer careerId, Pageable pageable);

  @Query("SELECT e.status, COUNT(e) FROM EnrollmentJpaEntity e GROUP BY e.status")
  List<Object[]> countGroupByStatus();

  @Query("""
      SELECT t.code, COUNT(e)
      FROM EnrollmentJpaEntity e
      JOIN CareerOfferingJpaEntity co ON e.careerOfferingId = co.id
      JOIN TermJpaEntity t ON co.termId = t.id
      GROUP BY t.code, t.startDate
      ORDER BY t.startDate ASC
    """)
  List<Object[]> countGroupByTerm();

  @Query("""
      SELECT c.name, COUNT(e)
      FROM EnrollmentJpaEntity e
      JOIN CareerOfferingJpaEntity co ON e.careerOfferingId = co.id
      JOIN CareerJpaEntity c ON co.careerId = c.careerId
      GROUP BY c.name
      ORDER BY COUNT(e) DESC
    """)
  List<Object[]> countGroupByCareer(Pageable pageable);

  @Query("""
      SELECT COUNT(e)
      FROM EnrollmentJpaEntity e
      JOIN CareerOfferingJpaEntity co ON e.careerOfferingId = co.id
      WHERE co.termId = :termId
    """)
  long countByTermId(@Param("termId") Integer termId);
}
