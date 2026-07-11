package com.app.enrollment.system.enrollment.server.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.common.events.EnrollmentAssignedEvent;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.SendEnrollmentEvent;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.query.EnrollmentQuery;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.PageResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.PaymentPreferenceResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerOfferingMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.EnrollmentMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.StudentMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.out.OutboxEventPort;
import com.app.enrollment.system.enrollment.server.application.port.out.PaymentGatewayPort;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentAlreadyEnrolledException;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.EnrollmentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.PageResult;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnrollmentApplicationServiceTest {

  @Mock
  private EnrollmentRepository enrollmentRepository;
  @Mock
  private CareerOfferingRepository careerOfferingRepository;
  @Mock
  private StudentRepository studentRepository;
  @Mock
  private OutboxEventPort outboxEventPort;
  @Mock
  private CareerRepository careerRepository;
  @Mock
  private TermRepository termRepository;
  @Mock
  private PaymentGatewayPort paymentGatewayPort;

  private EnrollmentApplicationService service;

  @BeforeEach
  void setUp() {
    // Mappers reales: son puros y sin dependencias externas.
    EnrollmentMapper enrollmentMapper =
      new EnrollmentMapper(new StudentMapper(), new CareerOfferingMapper());
    service = new EnrollmentApplicationService(enrollmentRepository, Mothers.fixedClock(),
      enrollmentMapper, careerOfferingRepository, studentRepository, outboxEventPort,
      careerRepository, careerMapper(), termRepository, new TermMapper(), paymentGatewayPort);
  }

  private CareerMapper careerMapper() {
    return new CareerMapper();
  }

  private void stubCatalog(CareerOffering offering) {
    when(careerOfferingRepository.findById(offering.getId())).thenReturn(Optional.of(offering));
    when(studentRepository.findById(any())).thenReturn(Optional.of(Mothers.student(1)));
    when(careerRepository.findById(offering.getCareerId()))
      .thenReturn(Optional.of(Mothers.career(1, "Diseño de Software")));
    when(termRepository.findById(offering.getTermId()))
      .thenReturn(Optional.of(Mothers.term(1, "2026-I")));
  }

  @Test
  void createEnrollmentSavesPendingIncrementsSeatAndEmitsEventWithPaymentUrl() {
    CareerOffering offering = Mothers.careerOffering(1, 1, 1);
    stubCatalog(offering);
    when(enrollmentRepository.findByStudentIDAndCareerOfferingIDAndStatusIn(any(), any(),
      anyCollection())).thenReturn(Optional.empty());
    when(enrollmentRepository.save(any()))
      .thenReturn(Mothers.enrollment(10, 1, 1, EnrollmentStatus.PENDING));
    when(paymentGatewayPort.createPreference(any()))
      .thenReturn(new PaymentPreferenceResponse("pref-1", "https://mp.test/init"));

    EnrollmentResponse response =
      service.createEnrollment(new CreateEnrollmentCommand(1, 1), 99);

    assertThat(response.getStatus()).isEqualTo(EnrollmentStatus.PENDING);
    assertThat(offering.getEnrolledCount()).isEqualTo(1);
    verify(careerOfferingRepository).save(offering);

    ArgumentCaptor<SendEnrollmentEvent> eventCaptor =
      ArgumentCaptor.forClass(SendEnrollmentEvent.class);
    verify(outboxEventPort).saveEvent(eventCaptor.capture());
    EnrollmentAssignedEvent payload =
      (EnrollmentAssignedEvent) eventCaptor.getValue().payload();
    assertThat(payload.getPaymentUrl()).isEqualTo("https://mp.test/init");
    assertThat(payload.getCareerName()).isEqualTo("Diseño de Software");
    assertThat(payload.getTermCode()).isEqualTo("2026-I");
  }

  @Test
  void createEnrollmentStillSucceedsWhenPaymentGatewayFails() {
    // Fail-soft: si Mercado Pago está caído, la matrícula igual se registra y
    // el evento sale sin enlace de pago.
    CareerOffering offering = Mothers.careerOffering(1, 1, 1);
    stubCatalog(offering);
    when(enrollmentRepository.findByStudentIDAndCareerOfferingIDAndStatusIn(any(), any(),
      anyCollection())).thenReturn(Optional.empty());
    when(enrollmentRepository.save(any()))
      .thenReturn(Mothers.enrollment(10, 1, 1, EnrollmentStatus.PENDING));
    when(paymentGatewayPort.createPreference(any()))
      .thenThrow(new RuntimeException("MP down"));

    EnrollmentResponse response =
      service.createEnrollment(new CreateEnrollmentCommand(1, 1), 99);

    assertThat(response.getStatus()).isEqualTo(EnrollmentStatus.PENDING);

    ArgumentCaptor<SendEnrollmentEvent> eventCaptor =
      ArgumentCaptor.forClass(SendEnrollmentEvent.class);
    verify(outboxEventPort).saveEvent(eventCaptor.capture());
    EnrollmentAssignedEvent payload =
      (EnrollmentAssignedEvent) eventCaptor.getValue().payload();
    assertThat(payload.getPaymentUrl()).isNull();
  }

  @Test
  void createEnrollmentRejectsStudentWithActiveEnrollment() {
    when(enrollmentRepository.findByStudentIDAndCareerOfferingIDAndStatusIn(any(), any(),
      anyCollection()))
      .thenReturn(Optional.of(Mothers.enrollment(5, 1, 1, EnrollmentStatus.PENDING)));

    assertThatThrownBy(() -> service.createEnrollment(new CreateEnrollmentCommand(1, 1), 99))
      .isInstanceOf(StudentAlreadyEnrolledException.class);

    verify(enrollmentRepository, never()).save(any());
    verify(outboxEventPort, never()).saveEvent(any());
  }

  @Test
  void getAllEnrollmentsAcceptsEmptyFilters() {
    // Regresión: con filtros vacíos se construía CareerID(null) y explotaba el listado.
    CareerOffering offering = Mothers.careerOffering(1, 1, 1);
    when(enrollmentRepository.findAllByStudentIDAndTermIDAndCareerID(isNull(), isNull(), isNull(),
      eq(0), eq(10)))
      .thenReturn(new PageResult<>(List.of(Mothers.enrollment(10, 1, 1, EnrollmentStatus.PAID)),
        0, 10, 1, 1));
    stubCatalog(offering);

    PageResponse<EnrollmentResponse> page =
      service.getAllEnrollmentCourses(new EnrollmentQuery(null, null, null, 0, 10));

    assertThat(page.content()).hasSize(1);
    assertThat(page.content().get(0).getCareerOffering().getPrice())
      .isEqualByComparingTo("350.00");
  }

  @Test
  void updateEnrollmentToCancelledReleasesSeat() {
    CareerOffering offering = Mothers.careerOffering(1, 1, 1, 30, 5, true,
      new java.math.BigDecimal("350.00"));
    Enrollment enrollment = Mothers.enrollment(10, 1, 1, EnrollmentStatus.PAID);
    when(enrollmentRepository.findByEnrollmentID(any())).thenReturn(Optional.of(enrollment));
    when(enrollmentRepository.save(any())).thenReturn(enrollment);
    stubCatalog(offering);

    service.updateEnrollment(10, new UpdateEnrollmentCommand(EnrollmentStatus.CANCELLED),
      new UserID(99));

    assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
    assertThat(offering.getEnrolledCount()).isEqualTo(4);
    verify(careerOfferingRepository).save(offering);
    verify(outboxEventPort).saveEvent(any());
  }
}
