package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.EnrollmentJpaEntity;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class EnrollmentJpaMapper {
  
  private final Clock clock;
  
  public EnrollmentJpaMapper(Clock clock) {
    this.clock = clock;
  }
  
  public Enrollment toDomainEntity(EnrollmentJpaEntity entity) {
    EnrollmentID enrollmentID =
      entity.getEnrollmentId() != null ? new EnrollmentID(entity.getEnrollmentId()) : null;
    
    StudentID studentID =
      entity.getStudentId() != null ? new StudentID(entity.getStudentId()) : null;
    
    CourseOfferingID courseOfferingID =
      entity.getCourseOfferingId() != null ? new CourseOfferingID(entity.getCourseOfferingId())
        : null;
    
    Instant unrolledAt =
      entity.getUnenrollmentDate() != null ? entity.getUnenrollmentDate().atZone(clock.getZone())
        .toInstant() : null;
    
    Instant enrollmentDate =
      entity.getEnrollmentDate() != null ? entity.getEnrollmentDate().atZone(clock.getZone())
        .toInstant() : null;
    
    UserID userID = new UserID(entity.getUserId());
    
    return Enrollment.rehydrate(enrollmentID, studentID, courseOfferingID, enrollmentDate, unrolledAt,
      entity.getStatus(), userID);
  }
  
  public EnrollmentJpaEntity toJpaEntity(Enrollment enrollment) {
    
    LocalDateTime enrollmentDate = LocalDateTime.ofInstant(enrollment.getEnrollmentDate(),
      clock.getZone());
    
    LocalDateTime unenrollmentDate =
      enrollment.getUnenrollmentDate() != null ? LocalDateTime.ofInstant(
        enrollment.getUnenrollmentDate(), clock.getZone()) : null;
    
    Integer enrollmentID = enrollment.getID() != null ? enrollment.getID().getValue() : null;
    
    return new EnrollmentJpaEntity(enrollmentID, enrollment.getStudentID().getValue(),
      enrollment.getCourseOfferingID().getValue(), enrollmentDate, unenrollmentDate, enrollment.getStatus(),
      enrollment.getUserID().getValue());
  }
}
