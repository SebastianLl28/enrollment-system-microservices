package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.Classroom;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import com.app.enrollment.system.enrollment.server.domain.repository.ClassroomRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.ClassroomJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.ClassroomJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.ClassroomJpaMapper;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class ClassroomRepositoryAdapter implements ClassroomRepository {

  private final ClassroomJpaRepository classroomJpaRepository;
  private final ClassroomJpaMapper classroomJpaMapper;

  public ClassroomRepositoryAdapter(ClassroomJpaRepository classroomJpaRepository,
    ClassroomJpaMapper classroomJpaMapper) {
    this.classroomJpaRepository = classroomJpaRepository;
    this.classroomJpaMapper = classroomJpaMapper;
  }

  @Override
  public Classroom save(Classroom classroom) {
    ClassroomJpaEntity classroomJpaEntity = classroomJpaMapper.toJpaEntity(classroom);
    ClassroomJpaEntity savedClassroomJpaEntity = classroomJpaRepository.save(classroomJpaEntity);
    return classroomJpaMapper.toDomainEntity(savedClassroomJpaEntity);
  }

  @Override
  public Optional<Classroom> findById(ClassroomID classroomID) {
    Integer id = classroomID.getValue();
    return classroomJpaRepository.findById(id).map(classroomJpaMapper::toDomainEntity);
  }

  @Override
  public List<Classroom> findAll() {
    return classroomJpaRepository.findAll().stream()
      .map(classroomJpaMapper::toDomainEntity)
      .toList();
  }
}
