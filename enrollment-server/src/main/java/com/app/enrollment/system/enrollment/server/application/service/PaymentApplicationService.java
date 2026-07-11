package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.common.enums.SourceServiceType;
import com.app.common.events.EnrollmentAssignedEvent;
import com.app.enrollment.system.enrollment.server.application.dto.command.SendEnrollmentEvent;
import com.app.enrollment.system.enrollment.server.application.dto.response.PaymentDetailsResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.ProcessPaymentNotificationUseCase;
import com.app.enrollment.system.enrollment.server.application.port.out.OutboxEventPort;
import com.app.enrollment.system.enrollment.server.application.port.out.PaymentGatewayPort;
import com.app.enrollment.system.enrollment.server.domain.event.EventType;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerOfferingNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.EnrollmentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.TermNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.EnrollmentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import java.time.Clock;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Procesa las notificaciones de pago de Mercado Pago: consulta el pago en la API
 * oficial, y si está aprobado marca la matrícula como PAID (idempotente) y
 * registra el evento de dominio en el outbox para notificar al estudiante.
 *
 * @author Alonso
 */
@UseCase
public class PaymentApplicationService implements ProcessPaymentNotificationUseCase {

  private static final Logger log = LoggerFactory.getLogger(PaymentApplicationService.class);

  private static final String MP_STATUS_APPROVED = "approved";

  private final PaymentGatewayPort paymentGatewayPort;
  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final CareerOfferingRepository careerOfferingRepository;
  private final CareerRepository careerRepository;
  private final TermRepository termRepository;
  private final OutboxEventPort outboxEventPort;
  private final Clock clock;

  public PaymentApplicationService(PaymentGatewayPort paymentGatewayPort,
    EnrollmentRepository enrollmentRepository, StudentRepository studentRepository,
    CareerOfferingRepository careerOfferingRepository, CareerRepository careerRepository,
    TermRepository termRepository, OutboxEventPort outboxEventPort, Clock clock) {
    this.paymentGatewayPort = paymentGatewayPort;
    this.enrollmentRepository = enrollmentRepository;
    this.studentRepository = studentRepository;
    this.careerOfferingRepository = careerOfferingRepository;
    this.careerRepository = careerRepository;
    this.termRepository = termRepository;
    this.outboxEventPort = outboxEventPort;
    this.clock = clock;
  }

  @Override
  @Transactional
  public void processPaymentNotification(String paymentId) {

    PaymentDetailsResponse payment = paymentGatewayPort.getPayment(paymentId);

    if (!MP_STATUS_APPROVED.equalsIgnoreCase(payment.status())) {
      log.info("Payment {} has status '{}', nothing to do", paymentId, payment.status());
      return;
    }

    Integer enrollmentId = parseEnrollmentId(payment.externalReference(), paymentId);
    if (enrollmentId == null) {
      return;
    }

    Enrollment enrollment = enrollmentRepository.findByEnrollmentID(new EnrollmentID(enrollmentId))
      .orElseThrow(() -> new EnrollmentNotFoundException(
        "Enrollment not found with id: " + enrollmentId + " (external_reference of payment "
          + paymentId + ")"));

    Instant paidAt = payment.dateApproved() != null ? payment.dateApproved() : clock.instant();

    boolean updated = enrollment.markAsPaid(payment.paymentId(), payment.status(), paidAt);

    if (!updated) {
      log.info("Enrollment {} is already PAID, skipping duplicate notification for payment {}",
        enrollmentId, paymentId);
      return;
    }

    Enrollment paidEnrollment = enrollmentRepository.save(enrollment);

    saveOutboxEvent(paidEnrollment, paidAt);

    log.info("Enrollment {} marked as PAID with payment {}", enrollmentId, paymentId);
  }

  private Integer parseEnrollmentId(String externalReference, String paymentId) {
    try {
      return Integer.valueOf(externalReference);
    } catch (NumberFormatException e) {
      log.warn("Payment {} has an external_reference that is not an enrollment id: '{}'",
        paymentId, externalReference);
      return null;
    }
  }

  private void saveOutboxEvent(Enrollment enrollment, Instant paidAt) {

    Student student = studentRepository.findById(enrollment.getStudentID()).orElseThrow(
      () -> new StudentNotFoundException(
        "Student not found with id: " + enrollment.getStudentID().getValue()));

    CareerOffering careerOffering = careerOfferingRepository.findById(
      enrollment.getCareerOfferingID()).orElseThrow(
      () -> new CareerOfferingNotFoundException(
        "Career offering not found with id: " + enrollment.getCareerOfferingID().getValue()));

    Career career = careerRepository.findById(careerOffering.getCareerId()).orElseThrow(
      () -> new CareerNotFoundException(
        "Career not found with id: " + careerOffering.getCareerId().getValue()));

    Term term = termRepository.findById(careerOffering.getTermId()).orElseThrow(
      () -> new TermNotFoundException(
        "Term not found with id: " + careerOffering.getTermId().getValue()));

    EnrollmentAssignedEvent event = new EnrollmentAssignedEvent(
      enrollment.getID().getValue().toString(),
      paidAt,
      student.getFullName(), student.getEmail().getValue(),
      enrollment.getStatus().toString(),
      enrollment.getUserID().getValue(),
      career.getName(),
      term.getCode()
    );

    SendEnrollmentEvent sendEnrollmentEvent = new SendEnrollmentEvent(
      SourceServiceType.ENROLLMENT_SERVICE.name(),
      enrollment.getID().getValue().toString(),
      EventType.ENROLLMENT_EVENT,
      event,
      enrollment.getUserID(),
      enrollment.getStatus(),
      student,
      career
    );

    outboxEventPort.saveEvent(sendEnrollmentEvent);
  }
}
