package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCareerOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCareerOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerOfferingResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerOfferingMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCareerOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCareerOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCareerOfferingUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerOfferingNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCareerOfferingException;
import com.app.enrollment.system.enrollment.server.domain.exception.TermNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import java.time.Clock;
import java.util.List;

/**
 * @author Alonso
 */
@UseCase
public class CareerOfferingApplicationService implements CreateCareerOfferingUseCase,
  GetAllCareerOfferingUseCase, UpdateCareerOfferingUseCase {

  private final CareerRepository careerRepository;
  private final TermRepository termRepository;
  private final CareerOfferingRepository careerOfferingRepository;
  private final CareerOfferingMapper careerOfferingMapper;
  private final CareerMapper careerMapper;
  private final TermMapper termMapper;
  private final Clock clock;

  public CareerOfferingApplicationService(CareerRepository careerRepository,
    TermRepository termRepository, CareerOfferingRepository careerOfferingRepository,
    CareerOfferingMapper careerOfferingMapper, CareerMapper careerMapper, TermMapper termMapper,
    Clock clock) {
    this.careerRepository = careerRepository;
    this.termRepository = termRepository;
    this.careerOfferingRepository = careerOfferingRepository;
    this.careerOfferingMapper = careerOfferingMapper;
    this.careerMapper = careerMapper;
    this.termMapper = termMapper;
    this.clock = clock;
  }

  @Override
  public CareerOfferingResponse createCareerOffering(CreateCareerOfferingCommand command) {
    CareerID careerID = new CareerID(command.careerId());
    TermID termID = new TermID(command.termId());

    Career career = careerRepository.findById(careerID).orElseThrow(
      () -> new CareerNotFoundException("Career with ID " + command.careerId() + " not found.")
    );

    Term term = termRepository.findById(termID).orElseThrow(
      () -> new TermNotFoundException("Term with ID " + command.termId() + " not found.")
    );

    CareerOffering careerOffering = CareerOffering.create(
      careerID,
      termID,
      command.capacity(),
      command.price(),
      clock.instant()
    );

    CareerOffering savedCareerOffering = careerOfferingRepository.save(careerOffering);

    CareerSummaryResponse careerSummaryResponse = careerMapper.toSummaryResponse(career);

    TermResponse termResponse = termMapper.toTermResponse(term);

    return careerOfferingMapper.toCareerOfferingResponse(savedCareerOffering,
      careerSummaryResponse, termResponse);
  }

  @Override
  public CareerOfferingResponse updateCareerOffering(UpdateCareerOfferingCommand command,
    Integer careerOfferingId) {
    CareerOfferingID careerOfferingID = new CareerOfferingID(careerOfferingId);
    CareerOffering existing = careerOfferingRepository.findById(careerOfferingID).orElseThrow(
      () -> new CareerOfferingNotFoundException(
        "Career offering with ID " + careerOfferingId + " not found.")
    );

    CareerID careerID = new CareerID(command.careerId());
    TermID termID = new TermID(command.termId());

    Career career = careerRepository.findById(careerID).orElseThrow(
      () -> new CareerNotFoundException("Career with ID " + command.careerId() + " not found.")
    );

    Term term = termRepository.findById(termID).orElseThrow(
      () -> new TermNotFoundException("Term with ID " + command.termId() + " not found.")
    );

    // La capacidad no puede quedar por debajo de los estudiantes ya matriculados.
    Integer enrolledCount = existing.getEnrolledCount() != null ? existing.getEnrolledCount() : 0;
    if (command.capacity() < enrolledCount) {
      throw new InvalidCareerOfferingException(
        "La capacidad (" + command.capacity() + ") no puede ser menor a los matriculados actuales ("
          + enrolledCount + ").");
    }

    CareerOffering updated = CareerOffering.rehydrate(careerOfferingID, careerID, termID,
      command.capacity(), enrolledCount, command.active(), existing.getCreatedAt(),
      command.price());

    CareerOffering saved = careerOfferingRepository.save(updated);

    CareerSummaryResponse careerSummaryResponse = careerMapper.toSummaryResponse(career);
    TermResponse termResponse = termMapper.toTermResponse(term);
    return careerOfferingMapper.toCareerOfferingResponse(saved, careerSummaryResponse, termResponse);
  }

  @Override
  public List<CareerOfferingResponse> getAllCareerOfferings() {
    List<CareerOffering> careerOfferingList = careerOfferingRepository.findAll();
    return careerOfferingList.stream().map(careerOffering -> {
      Career career = careerRepository.findById(careerOffering.getCareerId()).orElseThrow(
        () -> new CareerNotFoundException(
          "Career with ID " + careerOffering.getCareerId().getValue() + " not found.")
      );
      Term term = termRepository.findById(careerOffering.getTermId()).orElseThrow(
        () -> new TermNotFoundException(
          "Term with ID " + careerOffering.getTermId().getValue() + " not found.")
      );
      CareerSummaryResponse careerSummaryResponse = careerMapper.toSummaryResponse(career);
      TermResponse termResponse = termMapper.toTermResponse(term);
      return careerOfferingMapper.toCareerOfferingResponse(careerOffering, careerSummaryResponse,
        termResponse);
    }).toList();
  }
}
