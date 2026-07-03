package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateTermCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateTermCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateTermUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllTermUserCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateTermUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidTermException;
import com.app.enrollment.system.enrollment.server.domain.exception.OverlappingTermException;
import com.app.enrollment.system.enrollment.server.domain.exception.TermNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Alonso
 */
@UseCase
public class TermApplicationService implements CreateTermUseCase, GetAllTermUserCase,
  UpdateTermUseCase {

  private final TermRepository termRepository;
  private final Clock clock;
  private final TermMapper termMapper;

  public TermApplicationService(TermRepository termRepository, Clock clock, TermMapper termMapper) {
    this.termRepository = termRepository;
    this.clock = clock;
    this.termMapper = termMapper;
  }

  @Override
  public TermResponse createTerm(CreateTermCommand command) {
    validateDatesAndOverlap(command.startDate(), command.endDate(), null);
    Term term = Term.create(command.code(), command.startDate(), command.endDate(), clock.instant());
    term = termRepository.save(term);
    return termMapper.toTermResponse(term);
  }

  @Override
  public TermResponse updateTerm(UpdateTermCommand command, Integer termId) {
    TermID termID = new TermID(termId);
    Term existing = termRepository.findById(termID).orElseThrow(
      () -> new TermNotFoundException("Term with ID " + termId + " not found."));

    validateDatesAndOverlap(command.startDate(), command.endDate(), termId);

    Term updated = Term.rehydrate(termID, command.code(), command.startDate(),
      command.endDate(), command.active(), existing.getCreateAt());

    Term saved = termRepository.save(updated);
    return termMapper.toTermResponse(saved);
  }

  @Override
  public List<TermResponse> getAllTerms() {
    List<Term> terms = termRepository.findAll();
    return terms.stream()
      .map(termMapper::toTermResponse)
      .toList();
  }

  /**
   * Valida el rango de fechas y hace cumplir la regla de negocio: no pueden
   * existir dos vigencias con rangos de fechas que se solapen (dos vigencias
   * al mismo tiempo). En edición se excluye la propia vigencia por su id.
   */
  private void validateDatesAndOverlap(LocalDate startDate, LocalDate endDate, Integer excludeId) {
    if (startDate == null || endDate == null) {
      throw new InvalidTermException("La fecha de inicio y la fecha de fin son obligatorias.");
    }
    if (startDate.isAfter(endDate)) {
      throw new InvalidTermException(
        "La fecha de inicio no puede ser posterior a la fecha de fin.");
    }

    boolean overlaps = termRepository.findAll().stream()
      .filter(t -> excludeId == null || !t.getId().getValue().equals(excludeId))
      .anyMatch(t -> t.overlaps(startDate, endDate));

    if (overlaps) {
      throw new OverlappingTermException(
        "Ya existe una vigencia en ese rango de fechas. No pueden haber dos vigencias al mismo tiempo.");
    }
  }
}
