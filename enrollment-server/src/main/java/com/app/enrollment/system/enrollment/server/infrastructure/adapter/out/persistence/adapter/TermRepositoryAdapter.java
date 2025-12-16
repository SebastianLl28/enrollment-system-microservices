package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.TermJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.TermJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.TermJpaMapper;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class TermRepositoryAdapter implements TermRepository {
  
  private final TermJpaMapper termJpaMapper;
  private final TermJpaRepository termJpaRepository;
  
  public TermRepositoryAdapter(TermJpaMapper termJpaMapper, TermJpaRepository termJpaRepository) {
    this.termJpaMapper = termJpaMapper;
    this.termJpaRepository = termJpaRepository;
  }
  
  @Override
  public Term save(Term term) {
    TermJpaEntity termJpaEntity = termJpaMapper.toJpaEntity(term);
    TermJpaEntity savedTermJpaEntity = termJpaRepository.save(termJpaEntity);
    return termJpaMapper.toDomainEntity(savedTermJpaEntity);
  }
  
  @Override
  public Optional<Term> findByCode(String code) {
    Optional<TermJpaEntity> termJpaEntityOptional = termJpaRepository.findByCode(code);
    return termJpaEntityOptional
      .map(termJpaMapper::toDomainEntity);
  }
  
  @Override
  public Optional<Term> findById(TermID id) {
    Integer value = id.getValue();
    Optional<TermJpaEntity> termJpaEntityOptional = termJpaRepository.findById(value);
    return termJpaEntityOptional
      .map(termJpaMapper::toDomainEntity);
  }
  
  
  @Override
  public List<Term> findAll() {
    List<TermJpaEntity> termJpaEntities = termJpaRepository.findAll();
    return termJpaEntities.stream()
      .map(termJpaMapper::toDomainEntity)
      .toList();
  }
}
