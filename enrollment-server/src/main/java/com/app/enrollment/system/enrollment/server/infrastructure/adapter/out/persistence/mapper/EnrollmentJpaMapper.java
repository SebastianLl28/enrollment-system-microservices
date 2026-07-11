package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
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

    CareerOfferingID careerOfferingID =
      entity.getCareerOfferingId() != null ? new CareerOfferingID(entity.getCareerOfferingId())
        : null;

    Instant unrolledAt =
      entity.getUnenrollmentDate() != null ? entity.getUnenrollmentDate().atZone(clock.getZone())
        .toInstant() : null;

    Instant enrollmentDate =
      entity.getEnrollmentDate() != null ? entity.getEnrollmentDate().atZone(clock.getZone())
        .toInstant() : null;

    UserID userID = new UserID(entity.getUserId());

    Instant paidAt =
      entity.getPaidAt() != null ? entity.getPaidAt().atZone(clock.getZone()).toInstant() : null;

    return Enrollment.rehydrate(enrollmentID, studentID, careerOfferingID, enrollmentDate, unrolledAt,
      entity.getStatus(), userID, entity.getPaymentId(), entity.getPaymentStatus(), paidAt);
  }

  public EnrollmentJpaEntity toJpaEntity(Enrollment enrollment) {

    LocalDateTime enrollmentDate = LocalDateTime.ofInstant(enrollment.getEnrollmentDate(),
      clock.getZone());

    LocalDateTime unenrollmentDate =
      enrollment.getUnenrollmentDate() != null ? LocalDateTime.ofInstant(
        enrollment.getUnenrollmentDate(), clock.getZone()) : null;

    Integer enrollmentID = enrollment.getID() != null ? enrollment.getID().getValue() : null;

    EnrollmentJpaEntity entity = new EnrollmentJpaEntity(enrollmentID, enrollment.getStudentID().getValue(),
      enrollment.getCareerOfferingID().getValue(), enrollmentDate, unenrollmentDate, enrollment.getStatus(),
      enrollment.getUserID().getValue());

    entity.setPaymentId(enrollment.getPaymentId());
    entity.setPaymentStatus(enrollment.getPaymentStatus());
    entity.setPaidAt(enrollment.getPaidAt() != null
      ? LocalDateTime.ofInstant(enrollment.getPaidAt(), clock.getZone()) : null);

    return entity;
  }
}
