package com.app.enrollment.system.enrollment.server.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCareerOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCareerOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerOfferingResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerOfferingMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCareerOfferingException;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CareerOfferingApplicationServiceTest {

  @Mock
  private CareerRepository careerRepository;
  @Mock
  private TermRepository termRepository;
  @Mock
  private CareerOfferingRepository careerOfferingRepository;

  private CareerOfferingApplicationService service;

  @BeforeEach
  void setUp() {
    service = new CareerOfferingApplicationService(careerRepository, termRepository,
      careerOfferingRepository, new CareerOfferingMapper(), new CareerMapper(), new TermMapper(),
      Mothers.fixedClock());
  }

  @Test
  void createOfferingReturnsCareerTermAndPrice() {
    when(careerRepository.findById(any()))
      .thenReturn(Optional.of(Mothers.career(1, "Diseño de Software")));
    when(termRepository.findById(any())).thenReturn(Optional.of(Mothers.term(2, "2026-I")));
    when(careerOfferingRepository.save(any()))
      .thenReturn(Mothers.careerOffering(9, 1, 2, 30, 0, true, new BigDecimal("350.00")));

    CareerOfferingResponse response = service.createCareerOffering(
      new CreateCareerOfferingCommand(1, 2, 30, new BigDecimal("350.00")));

    assertThat(response.getCareer().getName()).isEqualTo("Diseño de Software");
    assertThat(response.getTerm().getCode()).isEqualTo("2026-I");
    assertThat(response.getPrice()).isEqualByComparingTo("350.00");
  }

  @Test
  void createOfferingRejectsUnknownCareer() {
    when(careerRepository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.createCareerOffering(
      new CreateCareerOfferingCommand(1, 2, 30, new BigDecimal("350.00"))))
      .isInstanceOf(CareerNotFoundException.class);
  }

  @Test
  void updateRejectsCapacityBelowEnrolledCount() {
    when(careerOfferingRepository.findById(any()))
      .thenReturn(Optional.of(Mothers.careerOffering(9, 1, 2, 30, 12, true,
        new BigDecimal("350.00"))));
    when(careerRepository.findById(any()))
      .thenReturn(Optional.of(Mothers.career(1, "Diseño de Software")));
    when(termRepository.findById(any())).thenReturn(Optional.of(Mothers.term(2, "2026-I")));

    assertThatThrownBy(() -> service.updateCareerOffering(
      new UpdateCareerOfferingCommand(1, 2, 10, true, new BigDecimal("350.00")), 9))
      .isInstanceOf(InvalidCareerOfferingException.class);

    verify(careerOfferingRepository, never()).save(any());
  }
}
