package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.CannotChangeStatusOfCancelledEnrollmentException;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class EnrollmentTest {

  private static final Instant PAID_AT = Instant.parse("2026-07-11T12:00:00Z");

  @Test
  void createStartsPending() {
    Enrollment enrollment = Enrollment.create(new StudentID(1), new CareerOfferingID(2),
      Mothers.NOW, new UserID(3));

    assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.PENDING);
    assertThat(enrollment.getEnrollmentDate()).isEqualTo(Mothers.NOW);
    assertThat(enrollment.getUnenrollmentDate()).isNull();
  }

  @Test
  void markAsPaidTransitionsPendingToPaidAndStoresPaymentData() {
    Enrollment enrollment = Mothers.enrollment(1, 1, 1, EnrollmentStatus.PENDING);

    boolean updated = enrollment.markAsPaid("mp-123", "approved", PAID_AT);

    assertThat(updated).isTrue();
    assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.PAID);
    assertThat(enrollment.getPaymentId()).isEqualTo("mp-123");
    assertThat(enrollment.getPaymentStatus()).isEqualTo("approved");
    assertThat(enrollment.getPaidAt()).isEqualTo(PAID_AT);
  }

  @Test
  void markAsPaidIsIdempotentWhenAlreadyPaid() {
    Enrollment enrollment = Mothers.enrollment(1, 1, 1, EnrollmentStatus.PENDING);
    enrollment.markAsPaid("mp-123", "approved", PAID_AT);

    boolean updatedAgain = enrollment.markAsPaid("mp-999", "approved", PAID_AT.plusSeconds(60));

    assertThat(updatedAgain).isFalse();
    // El primer pago no se sobreescribe
    assertThat(enrollment.getPaymentId()).isEqualTo("mp-123");
    assertThat(enrollment.getPaidAt()).isEqualTo(PAID_AT);
  }

  @Test
  void markAsPaidRejectsCancelledEnrollment() {
    Enrollment enrollment = Mothers.enrollment(1, 1, 1, EnrollmentStatus.CANCELLED);

    assertThatThrownBy(() -> enrollment.markAsPaid("mp-123", "approved", PAID_AT))
      .isInstanceOf(CannotChangeStatusOfCancelledEnrollmentException.class);
  }

  @Test
  void unenrollCancelsAndSetsUnenrollmentDate() {
    Enrollment enrollment = Mothers.enrollment(1, 1, 1, EnrollmentStatus.PAID);

    enrollment.unenroll(PAID_AT);

    assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
    assertThat(enrollment.getUnenrollmentDate()).isEqualTo(PAID_AT);
  }

  @Test
  void unenrollIsIdempotent() {
    Enrollment enrollment = Mothers.enrollment(1, 1, 1, EnrollmentStatus.PAID);
    enrollment.unenroll(PAID_AT);

    enrollment.unenroll(PAID_AT.plusSeconds(60));

    // La segunda baja no cambia la fecha original
    assertThat(enrollment.getUnenrollmentDate()).isEqualTo(PAID_AT);
  }

  @Test
  void updateStatusRejectsCancelledEnrollment() {
    Enrollment enrollment = Mothers.enrollment(1, 1, 1, EnrollmentStatus.CANCELLED);

    assertThatThrownBy(() -> enrollment.updateStatus(EnrollmentStatus.COMPLETED))
      .isInstanceOf(CannotChangeStatusOfCancelledEnrollmentException.class);
  }

  @Test
  void updateStatusChangesNonCancelledEnrollment() {
    Enrollment enrollment = Mothers.enrollment(1, 1, 1, EnrollmentStatus.PAID);

    enrollment.updateStatus(EnrollmentStatus.COMPLETED);

    assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
  }
}
