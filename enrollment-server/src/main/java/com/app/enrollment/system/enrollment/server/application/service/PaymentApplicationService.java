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
import com.app.enrollment.system.enrollment.server.domain.exception.CourseNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseOfferingNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.EnrollmentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.EnrollmentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import java.time.Clock;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Procesa las notificaciones de pago de Mercado Pago: consulta el pago en la API
 * oficial, y si está aprobado marca la inscripción como PAID (idempotente) y
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
  private final CourseOfferingRepository courseOfferingRepository;
  private final CourseRepository courseRepository;
  private final OutboxEventPort outboxEventPort;
  private final Clock clock;

  public PaymentApplicationService(PaymentGatewayPort paymentGatewayPort,
    EnrollmentRepository enrollmentRepository, StudentRepository studentRepository,
    CourseOfferingRepository courseOfferingRepository, CourseRepository courseRepository,
    OutboxEventPort outboxEventPort, Clock clock) {
    this.paymentGatewayPort = paymentGatewayPort;
    this.enrollmentRepository = enrollmentRepository;
    this.studentRepository = studentRepository;
    this.courseOfferingRepository = courseOfferingRepository;
    this.courseRepository = courseRepository;
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

    CourseOffering courseOffering = courseOfferingRepository.findById(
      enrollment.getCourseOfferingID()).orElseThrow(
      () -> new CourseOfferingNotFoundException(
        "Course offering not found with id: " + enrollment.getCourseOfferingID().getValue()));

    Course course = courseRepository.findById(courseOffering.getCourseId()).orElseThrow(
      () -> new CourseNotFoundException(
        "Course not found with id: " + courseOffering.getCourseId().getValue()));

    EnrollmentAssignedEvent event = new EnrollmentAssignedEvent(
      enrollment.getID().getValue().toString(),
      enrollment.getStudentID().getValue().toString(),
      courseOffering.getCourseId().toString(), paidAt,
      student.getFullName(), student.getEmail().getValue(),
      enrollment.getStatus().toString(),
      enrollment.getUserID().getValue(),
      course.getName()
    );

    SendEnrollmentEvent sendEnrollmentEvent = new SendEnrollmentEvent(
      SourceServiceType.ENROLLMENT_SERVICE.name(),
      enrollment.getID().getValue().toString(),
      EventType.ENROLLMENT_EVENT,
      event,
      enrollment.getUserID(),
      enrollment.getStatus(),
      student,
      course
    );

    outboxEventPort.saveEvent(sendEnrollmentEvent);
  }
}
