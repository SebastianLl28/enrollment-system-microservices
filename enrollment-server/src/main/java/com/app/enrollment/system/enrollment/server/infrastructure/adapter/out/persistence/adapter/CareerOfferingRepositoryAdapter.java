package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerOfferingJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.CareerOfferingJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.CareerOfferingJpaMapper;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class CareerOfferingRepositoryAdapter implements CareerOfferingRepository {

  private final CareerOfferingJpaRepository careerOfferingJpaRepository;
  private final CareerOfferingJpaMapper careerOfferingJpaMapper;

  public CareerOfferingRepositoryAdapter(CareerOfferingJpaRepository careerOfferingJpaRepository,
    CareerOfferingJpaMapper careerOfferingJpaMapper) {
    this.careerOfferingJpaRepository = careerOfferingJpaRepository;
    this.careerOfferingJpaMapper = careerOfferingJpaMapper;
  }

  @Override
  public CareerOffering save(CareerOffering careerOffering) {
    CareerOfferingJpaEntity careerOfferingJpaEntity = careerOfferingJpaMapper.toJpaEntity(careerOffering);
    CareerOfferingJpaEntity savedCareerOfferingJpaEntity = careerOfferingJpaRepository.save(careerOfferingJpaEntity);
    return careerOfferingJpaMapper.toDomainEntity(savedCareerOfferingJpaEntity);
  }

  @Override
  public Optional<CareerOffering> findById(CareerOfferingID careerOfferingID) {
    Integer id = careerOfferingID.getValue();
    return careerOfferingJpaRepository.findById(id)
      .map(careerOfferingJpaMapper::toDomainEntity);
  }

  @Override
  public List<CareerOffering> findAll() {
    return careerOfferingJpaRepository.findAll().stream()
      .map(careerOfferingJpaMapper::toDomainEntity)
      .toList();
  }

  @Override
  public List<CareerOffering> findAllByTermId(TermID termID) {
    return careerOfferingJpaRepository.findByTermId(termID.getValue()).stream()
      .map(careerOfferingJpaMapper::toDomainEntity)
      .toList();
  }
}
