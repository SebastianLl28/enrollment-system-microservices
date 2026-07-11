package com.app.enrollment.system.enrollment.server.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.enrollment.system.enrollment.server.application.dto.response.PaymentDetailsResponse;
import com.app.enrollment.system.enrollment.server.application.port.out.OutboxEventPort;
import com.app.enrollment.system.enrollment.server.application.port.out.PaymentGatewayPort;
import com.app.enrollment.system.enrollment.server.domain.exception.EnrollmentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.EnrollmentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentApplicationServiceTest {

  private static final Instant APPROVED_AT = Instant.parse("2026-07-11T12:00:00Z");

  @Mock
  private PaymentGatewayPort paymentGatewayPort;
  @Mock
  private EnrollmentRepository enrollmentRepository;
  @Mock
  private StudentRepository studentRepository;
  @Mock
  private CareerOfferingRepository careerOfferingRepository;
  @Mock
  private CareerRepository careerRepository;
  @Mock
  private TermRepository termRepository;
  @Mock
  private OutboxEventPort outboxEventPort;

  private PaymentApplicationService service;

  @BeforeEach
  void setUp() {
    service = new PaymentApplicationService(paymentGatewayPort, enrollmentRepository,
      studentRepository, careerOfferingRepository, careerRepository, termRepository,
      outboxEventPort, Mothers.fixedClock());
  }

  @Test
  void approvedPaymentMarksEnrollmentAsPaidAndEmitsEvent() {
    Enrollment enrollment = Mothers.enrollment(10, 1, 1, EnrollmentStatus.PENDING);
    when(paymentGatewayPort.getPayment("mp-1"))
      .thenReturn(new PaymentDetailsResponse("mp-1", "approved", "10", APPROVED_AT));
    when(enrollmentRepository.findByEnrollmentID(any())).thenReturn(Optional.of(enrollment));
    when(enrollmentRepository.save(any())).thenReturn(enrollment);
    when(studentRepository.findById(any())).thenReturn(Optional.of(Mothers.student(1)));
    when(careerOfferingRepository.findById(any()))
      .thenReturn(Optional.of(Mothers.careerOffering(1, 1, 1)));
    when(careerRepository.findById(any()))
      .thenReturn(Optional.of(Mothers.career(1, "Diseño de Software")));
    when(termRepository.findById(any())).thenReturn(Optional.of(Mothers.term(1, "2026-I")));

    service.processPaymentNotification("mp-1");

    assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.PAID);
    assertThat(enrollment.getPaymentId()).isEqualTo("mp-1");
    assertThat(enrollment.getPaidAt()).isEqualTo(APPROVED_AT);
    verify(enrollmentRepository).save(enrollment);
    verify(outboxEventPort).saveEvent(any());
  }

  @Test
  void duplicateNotificationForPaidEnrollmentIsNoOp() {
    Enrollment enrollment = Mothers.enrollment(10, 1, 1, EnrollmentStatus.PENDING);
    enrollment.markAsPaid("mp-old", "approved", APPROVED_AT);
    when(paymentGatewayPort.getPayment("mp-1"))
      .thenReturn(new PaymentDetailsResponse("mp-1", "approved", "10", APPROVED_AT));
    when(enrollmentRepository.findByEnrollmentID(any())).thenReturn(Optional.of(enrollment));

    service.processPaymentNotification("mp-1");

    verify(enrollmentRepository, never()).save(any());
    verify(outboxEventPort, never()).saveEvent(any());
  }

  @Test
  void nonApprovedPaymentDoesNothing() {
    when(paymentGatewayPort.getPayment("mp-1"))
      .thenReturn(new PaymentDetailsResponse("mp-1", "pending", "10", null));

    service.processPaymentNotification("mp-1");

    verify(enrollmentRepository, never()).findByEnrollmentID(any());
    verify(outboxEventPort, never()).saveEvent(any());
  }

  @Test
  void nonNumericExternalReferenceIsIgnored() {
    when(paymentGatewayPort.getPayment("mp-1"))
      .thenReturn(new PaymentDetailsResponse("mp-1", "approved", "simulador-mp", APPROVED_AT));

    service.processPaymentNotification("mp-1");

    verify(enrollmentRepository, never()).findByEnrollmentID(any());
  }

  @Test
  void missingEnrollmentThrowsNotFound() {
    when(paymentGatewayPort.getPayment("mp-1"))
      .thenReturn(new PaymentDetailsResponse("mp-1", "approved", "10", APPROVED_AT));
    when(enrollmentRepository.findByEnrollmentID(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.processPaymentNotification("mp-1"))
      .isInstanceOf(EnrollmentNotFoundException.class);
  }
}
