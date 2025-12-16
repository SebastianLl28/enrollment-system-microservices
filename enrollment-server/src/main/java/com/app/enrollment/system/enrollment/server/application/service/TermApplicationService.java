package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateTermCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateTermUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllTermUserCase;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import java.time.Clock;
import java.util.List;

/**
 * @author Alonso
 */
@UseCase
public class TermApplicationService implements CreateTermUseCase, GetAllTermUserCase {
  
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
    Term term = Term.create(command.code(), command.startDate(), command.endDate(), clock.instant());
    term = termRepository.save(term);
    return termMapper.toTermResponse(term);
  }
  
  
  @Override
  public List<TermResponse> getAllTerms() {
    List<Term> terms = termRepository.findAll();
    return terms.stream()
      .map(termMapper::toTermResponse)
      .toList();
  }
}
