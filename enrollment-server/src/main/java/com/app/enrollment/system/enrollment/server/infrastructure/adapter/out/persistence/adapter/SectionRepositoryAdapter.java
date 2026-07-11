package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.Section;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SectionID;
import com.app.enrollment.system.enrollment.server.domain.repository.SectionRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.SectionJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.SectionJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.SectionJpaMapper;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class SectionRepositoryAdapter implements SectionRepository {

  private final SectionJpaRepository sectionJpaRepository;
  private final SectionJpaMapper sectionJpaMapper;

  public SectionRepositoryAdapter(SectionJpaRepository sectionJpaRepository,
    SectionJpaMapper sectionJpaMapper) {
    this.sectionJpaRepository = sectionJpaRepository;
    this.sectionJpaMapper = sectionJpaMapper;
  }

  @Override
  public Section save(Section section) {
    SectionJpaEntity sectionJpaEntity = sectionJpaMapper.toJpaEntity(section);
    SectionJpaEntity savedSectionJpaEntity = sectionJpaRepository.save(sectionJpaEntity);
    return sectionJpaMapper.toDomainEntity(savedSectionJpaEntity);
  }

  @Override
  public Optional<Section> findById(SectionID sectionID) {
    Integer id = sectionID.getValue();
    return sectionJpaRepository.findById(id).map(sectionJpaMapper::toDomainEntity);
  }

  @Override
  public List<Section> findAll() {
    return sectionJpaRepository.findAll().stream()
      .map(sectionJpaMapper::toDomainEntity)
      .toList();
  }
}
