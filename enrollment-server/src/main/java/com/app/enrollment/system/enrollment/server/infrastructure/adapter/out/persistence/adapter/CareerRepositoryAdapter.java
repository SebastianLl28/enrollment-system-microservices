package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.CareerJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.CareerJpaMapper;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class CareerRepositoryAdapter implements CareerRepository {

  private final CareerJpaRepository careerJpaRepository;
  private final CareerJpaMapper careerJpaMapper;

  public CareerRepositoryAdapter(CareerJpaRepository careerJpaRepository, CareerJpaMapper careerJpaMapper) {
    this.careerJpaRepository = careerJpaRepository;
    this.careerJpaMapper = careerJpaMapper;
  }

  @Override
  public Optional<Career> findById(CareerID careerID) {
    return careerJpaRepository.findById(careerID.getValue()).map(
        careerJpaMapper::toDomainCareer
    );
  }

  @Override
  public Career save(Career career) {
    CareerJpaEntity careerJpaEntity = careerJpaMapper.toJpaEntity(career);
    careerJpaEntity = careerJpaRepository.save(careerJpaEntity);
    return careerJpaMapper.toDomainCareer(careerJpaEntity);
  }

  @Override
  public List<Career> findAll() {
    return careerJpaRepository.findAll().stream().map(
        careerJpaMapper::toDomainCareer
    ).toList();
  }

//  @Override
//  public List<Career> findByFacultyId(Integer facultyId) {
//    return List.of();
//  }
//
//  @Override
//  public List<Career> findByActive(boolean active) {
//    return List.of();
//  }
//
//  @Override
//  public long countActiveCourses(Integer careerId) {
//    return 0;
//  }
}
