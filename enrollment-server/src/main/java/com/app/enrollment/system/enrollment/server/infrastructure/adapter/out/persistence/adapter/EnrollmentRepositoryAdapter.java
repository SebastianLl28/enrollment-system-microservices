package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.repository.EnrollmentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.PageResult;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.EnrollmentJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.EnrollmentJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.EnrollmentJpaMapper;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author Alonso
 */
@Adapter
public class EnrollmentRepositoryAdapter implements EnrollmentRepository {
  
  private final EnrollmentJpaMapper enrollmentJpaMapper;
  private final EnrollmentJpaRepository enrollmentJpaRepository;
  
  public EnrollmentRepositoryAdapter(EnrollmentJpaMapper enrollmentJpaMapper,
    EnrollmentJpaRepository enrollmentJpaRepository) {
    this.enrollmentJpaMapper = enrollmentJpaMapper;
    this.enrollmentJpaRepository = enrollmentJpaRepository;
  }
  
  @Override
  public Enrollment save(Enrollment enrollment) {
    EnrollmentJpaEntity enrollmentJpaEntity = enrollmentJpaMapper.toJpaEntity(enrollment);
    enrollmentJpaEntity = enrollmentJpaRepository.save(enrollmentJpaEntity);
    return enrollmentJpaMapper.toDomainEntity(enrollmentJpaEntity);
  }
  
  @Override
  public Optional<Enrollment> findByStudentIDAndCourseOfferingIDAndStatus(StudentID studentID,
    CourseOfferingID courseOfferingID, EnrollmentStatus enrollmentStatus) {
    Integer studentId = studentID.getValue();
    Integer courseOfferingId = courseOfferingID.getValue();
    return enrollmentJpaRepository.findByStudentIdAndCourseOfferingIdAndStatus(studentId,
      courseOfferingId, enrollmentStatus).map(enrollmentJpaMapper::toDomainEntity);
  }
  
  @Override
  public PageResult<Enrollment> findAllByStudentIDAndTermIDAndCourseID(StudentID studentID, TermID termID,
    CourseID courseID, int page, int size) {
    Integer studentId = studentID != null ? studentID.getValue() : null;
    Integer termId = termID != null ? termID.getValue() : null;
    Integer courseId = courseID != null ? courseID.getValue() : null;
    Page<EnrollmentJpaEntity> result = enrollmentJpaRepository.findAllByStudentIDAndTermIDAndCourseID(
      studentId, termId, courseId, PageRequest.of(page, size));
    return new PageResult<>(
      result.getContent().stream().map(enrollmentJpaMapper::toDomainEntity).toList(),
      result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
  }
  
  @Override
  public Optional<Enrollment> findByEnrollmentID(EnrollmentID enrollmentID) {
    Integer enrollmentId = enrollmentID.getValue();
    return enrollmentJpaRepository.findById(enrollmentId)
      .map(enrollmentJpaMapper::toDomainEntity);
  }
  
}
