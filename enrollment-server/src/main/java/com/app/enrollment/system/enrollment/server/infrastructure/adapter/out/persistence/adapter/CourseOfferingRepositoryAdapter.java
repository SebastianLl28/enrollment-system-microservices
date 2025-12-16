package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseOfferingRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CourseOfferingJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.CourseOfferingJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.CourseOfferingJpaMapper;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class CourseOfferingRepositoryAdapter implements CourseOfferingRepository {
  
  private final CourseOfferingJpaRepository courseOfferingJpaRepository;
  private final CourseOfferingJpaMapper courseOfferingJpaMapper;
  
  public CourseOfferingRepositoryAdapter(CourseOfferingJpaRepository courseOfferingJpaRepository,
    CourseOfferingJpaMapper courseOfferingJpaMapper) {
    this.courseOfferingJpaRepository = courseOfferingJpaRepository;
    this.courseOfferingJpaMapper = courseOfferingJpaMapper;
  }
  
  @Override
  public CourseOffering save(CourseOffering courseOffering) {
    CourseOfferingJpaEntity courseOfferingJpaEntity = courseOfferingJpaMapper.toJpaEntity(courseOffering);
    CourseOfferingJpaEntity savedCourseOfferingJpaEntity = courseOfferingJpaRepository.save(courseOfferingJpaEntity);
    return courseOfferingJpaMapper.toDomainEntity(savedCourseOfferingJpaEntity);
  }
  
  @Override
  public Optional<CourseOffering> findById(CourseOfferingID courseOfferingID) {
    Integer id = courseOfferingID.getValue();
    return courseOfferingJpaRepository.findById(id)
      .map(courseOfferingJpaMapper::toDomainEntity);
  }
  
  @Override
  public List<CourseOffering> findAll() {
    return courseOfferingJpaRepository.findAll().stream()
      .map(courseOfferingJpaMapper::toDomainEntity)
      .toList();
  }
}
