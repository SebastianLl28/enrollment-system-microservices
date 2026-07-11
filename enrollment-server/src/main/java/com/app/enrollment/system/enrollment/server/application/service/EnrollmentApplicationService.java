package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.common.enums.SourceServiceType;
import com.app.common.events.EnrollmentAssignedEvent;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreatePaymentPreferenceCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.SendEnrollmentEvent;
import com.app.enrollment.system.enrollment.server.application.dto.command.UnenrollStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.query.EnrollmentQuery;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.PageResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.PaymentPreferenceResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.EnrollmentMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateEnrollmentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllEnrollmentCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetEnrollmentByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UnenrollStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateEnrollmentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.out.OutboxEventPort;
import com.app.enrollment.system.enrollment.server.application.port.out.PaymentGatewayPort;
import com.app.enrollment.system.enrollment.server.domain.event.EventType;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerOfferingNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.EnrollmentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentAlreadyEnrolledException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.TermNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.EnrollmentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.PageResult;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alonso
 */
@UseCase
public class EnrollmentApplicationService implements CreateEnrollmentUseCase,
  UnenrollStudentUseCase, GetAllEnrollmentCourseUseCase, GetEnrollmentByIdUseCase,
  UpdateEnrollmentUseCase {

  private static final Logger log = LoggerFactory.getLogger(EnrollmentApplicationService.class);

  private final EnrollmentRepository enrollmentRepository;
  private final Clock clock;
  private final EnrollmentMapper enrollmentMapper;
  private final CareerOfferingRepository careerOfferingRepository;
  private final StudentRepository studentRepository;
  private final OutboxEventPort outboxEventPort;
  private final CareerRepository careerRepository;
  private final CareerMapper careerMapper;
  private final TermRepository termRepository;
  private final TermMapper termMapper;
  private final PaymentGatewayPort paymentGatewayPort;

  public EnrollmentApplicationService(EnrollmentRepository enrollmentRepository, Clock clock,
    EnrollmentMapper enrollmentMapper, CareerOfferingRepository careerOfferingRepository,
    StudentRepository studentRepository, OutboxEventPort outboxEventPort,
    CareerRepository careerRepository, CareerMapper careerMapper, TermRepository termRepository,
    TermMapper termMapper, PaymentGatewayPort paymentGatewayPort) {
    this.enrollmentRepository = enrollmentRepository;
    this.clock = clock;
    this.enrollmentMapper = enrollmentMapper;
    this.careerOfferingRepository = careerOfferingRepository;
    this.studentRepository = studentRepository;
    this.outboxEventPort = outboxEventPort;
    this.careerRepository = careerRepository;
    this.careerMapper = careerMapper;
    this.termRepository = termRepository;
    this.termMapper = termMapper;
    this.paymentGatewayPort = paymentGatewayPort;
  }

  @Override
  @Transactional
  public EnrollmentResponse createEnrollment(CreateEnrollmentCommand command, Integer userId) {

    StudentID studentID = new StudentID(command.studentId());
    CareerOfferingID careerOfferingID = new CareerOfferingID(command.careerOfferingId());
    Instant now = clock.instant();

    // Un estudiante no puede tener una matrícula activa (pendiente de pago o pagada)
    // para la misma carrera-periodo; solo puede rematricularse si la anterior fue cancelada.
    Optional<Enrollment> activeEnrollment =
      enrollmentRepository.findByStudentIDAndCareerOfferingIDAndStatusIn(
        studentID, careerOfferingID, EnumSet.of(EnrollmentStatus.PENDING, EnrollmentStatus.PAID));

    if (activeEnrollment.isPresent()) {
      throw new StudentAlreadyEnrolledException(
        "Student with id: " + studentID.getValue()
          + " is already enrolled in career offering with id: " + careerOfferingID.getValue());
    }

    CareerOffering careerOffering = careerOfferingRepository.findById(careerOfferingID).orElseThrow(
      () -> new CareerOfferingNotFoundException(
        "Career offering not found with id: " + careerOfferingID.getValue()));

    Student student = studentRepository.findById(studentID).orElseThrow(
      () -> new StudentNotFoundException("Student not found with id: " + studentID.getValue()));

    UserID userID = new UserID(userId);

    Enrollment enrollment = Enrollment.create(studentID, careerOfferingID, now, userID);

    Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

    careerOffering.incrementEnrolledCount();

    careerOfferingRepository.save(careerOffering);

    Career career = findCareer(careerOffering.getCareerId());

    Term term = findTerm(careerOffering.getTermId());

    String paymentUrl = createPaymentPreference(savedEnrollment, student, career, term,
      careerOffering);

    saveOutboxEvent(savedEnrollment, userID, now, student, career, term, paymentUrl);

    return getEnrollmentResponse(careerOffering, student, savedEnrollment);

  }

  /**
   * Crea la Preferencia de Pago (Checkout Pro) y devuelve el init_point. Si Mercado Pago
   * falla, la matrícula igual se registra (queda PENDING) y el correo sale sin enlace.
   */
  private String createPaymentPreference(Enrollment savedEnrollment, Student student,
    Career career, Term term, CareerOffering careerOffering) {
    try {
      PaymentPreferenceResponse preference = paymentGatewayPort.createPreference(
        new CreatePaymentPreferenceCommand(
          savedEnrollment.getID().getValue(),
          career.getName(),
          term.getCode(),
          student.getFullName(),
          student.getEmail().getValue(),
          careerOffering.getPrice()));
      return preference.initPoint();
    } catch (Exception e) {
      log.error("Could not create Mercado Pago preference for enrollment {}: {}",
        savedEnrollment.getID().getValue(), e.getMessage());
      return null;
    }
  }

  private EnrollmentResponse getEnrollmentResponse(CareerOffering careerOffering, Student student,
    Enrollment savedEnrollment) {
    Career career = findCareer(careerOffering.getCareerId());

    CareerSummaryResponse careerSummaryResponse = careerMapper.toSummaryResponse(career);

    Term term = findTerm(careerOffering.getTermId());

    TermResponse termResponse = termMapper.toTermResponse(term);

    return enrollmentMapper.toEnrollmentResponse(savedEnrollment, student, careerOffering,
      careerSummaryResponse, termResponse);
  }

  private Career findCareer(CareerID careerID) {
    return careerRepository.findById(careerID).orElseThrow(
      () -> new CareerNotFoundException("Career not found with id: " + careerID.getValue()));
  }

  private Term findTerm(TermID termID) {
    return termRepository.findById(termID).orElseThrow(
      () -> new TermNotFoundException("Term not found with id: " + termID.getValue()));
  }

  private CareerOffering findCareerOffering(CareerOfferingID careerOfferingID) {
    return careerOfferingRepository.findById(careerOfferingID).orElseThrow(
      () -> new CareerOfferingNotFoundException(
        "Career offering not found with id: " + careerOfferingID.getValue()));
  }

  private void saveOutboxEvent(Enrollment savedEnrollment, UserID userID, Instant now,
    Student student, Career career, Term term) {
    saveOutboxEvent(savedEnrollment, userID, now, student, career, term, null);
  }

  private void saveOutboxEvent(Enrollment savedEnrollment, UserID userID, Instant now,
    Student student, Career career, Term term, String paymentUrl) {

    EnrollmentAssignedEvent event = new EnrollmentAssignedEvent(
      savedEnrollment.getID().getValue().toString(),
      now,
      student.getFullName(), student.getEmail().getValue(),
      savedEnrollment.getStatus().toString(),
      userID.getValue(),
      career.getName(),
      term.getCode()
    );

    event.setPaymentUrl(paymentUrl);

    SendEnrollmentEvent sendEnrollmentEvent = new SendEnrollmentEvent(
      SourceServiceType.ENROLLMENT_SERVICE.name(),
      savedEnrollment.getID().getValue().toString(),
      EventType.ENROLLMENT_EVENT,
      event,
      userID,
      savedEnrollment.getStatus(),
      student,
      career
    );

    outboxEventPort.saveEvent(sendEnrollmentEvent);

  }


  @Override
  public EnrollmentResponse unenrollStudent(UnenrollStudentCommand command, UserID userID) {
    StudentID studentID = new StudentID(command.studentId());
    CareerOfferingID careerOfferingID = new CareerOfferingID(command.careerOfferingId());

    Enrollment enrollment = enrollmentRepository.findByStudentIDAndCareerOfferingIDAndStatusIn(
      studentID, careerOfferingID, EnumSet.of(EnrollmentStatus.COMPLETED)).orElseThrow(
      () -> new EnrollmentNotFoundException(
        "Enrollment not found for student id: " + studentID.getValue()
          + " and career offering id: " + careerOfferingID.getValue()));

    enrollment.unenroll(clock.instant());

    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

    Student student = studentRepository.findById(studentID).orElseThrow(
      () -> new StudentNotFoundException("Student not found with id: " + studentID.getValue()));

    CareerOffering careerOffering = findCareerOffering(careerOfferingID);

    careerOffering.decrementEnrolledCount();

    careerOfferingRepository.save(careerOffering);

    Career career = findCareer(careerOffering.getCareerId());

    Term term = findTerm(careerOffering.getTermId());

    saveOutboxEvent(enrollment, userID, clock.instant(), student, career, term);

    return getEnrollmentResponse(careerOffering, student, updatedEnrollment);

  }

  @Override
  public PageResponse<EnrollmentResponse> getAllEnrollmentCourses(EnrollmentQuery query) {
    StudentID studentID = new StudentID(query.studentId());
    TermID termID = new TermID(query.termId());
    CareerID careerID = new CareerID(query.careerId());

    PageResult<Enrollment> enrollmentPage = enrollmentRepository.findAllByStudentIDAndTermIDAndCareerID(
      studentID, termID, careerID, query.page(), query.size());

    List<EnrollmentResponse> content = enrollmentPage.content().stream().map(enrollment -> {

      Student student = studentRepository.findById(enrollment.getStudentID()).orElseThrow(
        () -> new StudentNotFoundException(
          "Student not found with id: " + enrollment.getStudentID().getValue()));

      CareerOffering careerOffering = findCareerOffering(enrollment.getCareerOfferingID());

      return getEnrollmentResponse(careerOffering, student, enrollment);
    }).toList();

    return new PageResponse<>(content, enrollmentPage.page(), enrollmentPage.size(),
      enrollmentPage.totalElements(), enrollmentPage.totalPages());
  }

  @Override
  public EnrollmentResponse getEnrollmentById(Integer enrollmentId, UserID userID) {
    EnrollmentID enrollmentID = new EnrollmentID(enrollmentId);

    Enrollment enrollment = enrollmentRepository.findByEnrollmentID(enrollmentID).orElseThrow(
      () -> new EnrollmentNotFoundException(
        "Enrollment not found with id: " + enrollmentID.getValue()));

    Student student = studentRepository.findById(enrollment.getStudentID()).orElseThrow(
      () -> new StudentNotFoundException(
        "Student not found with id: " + enrollment.getStudentID().getValue()));

    CareerOffering careerOffering = findCareerOffering(enrollment.getCareerOfferingID());

    return getEnrollmentResponse(careerOffering, student, enrollment);
  }

  @Override
  public EnrollmentResponse updateEnrollment(Integer enrollmentId,
    UpdateEnrollmentCommand command, UserID userID) {

    EnrollmentID enrollmentID = new EnrollmentID(enrollmentId);

    Enrollment enrollment = enrollmentRepository.findByEnrollmentID(enrollmentID).orElseThrow(
      () -> new EnrollmentNotFoundException(
        "Enrollment not found with id: " + enrollmentID.getValue()));

    Student student = studentRepository.findById(enrollment.getStudentID()).orElseThrow(
      () -> new StudentNotFoundException(
        "Student not found with id: " + enrollment.getStudentID().getValue()));

    CareerOffering careerOffering = findCareerOffering(enrollment.getCareerOfferingID());

    if (command.status() == EnrollmentStatus.CANCELLED) {
      enrollment.unenroll(clock.instant());
      careerOffering.decrementEnrolledCount();
    } else {
      enrollment.updateStatus(command.status());
    }

    careerOfferingRepository.save(careerOffering);

    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

    Career career = findCareer(careerOffering.getCareerId());

    Term term = findTerm(careerOffering.getTermId());

    saveOutboxEvent(updatedEnrollment, userID, clock.instant(), student, career, term);

    return getEnrollmentResponse(careerOffering, student, updatedEnrollment);

  }
}
