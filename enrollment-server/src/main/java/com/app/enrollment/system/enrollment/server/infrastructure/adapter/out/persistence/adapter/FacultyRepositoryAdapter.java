package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import com.app.enrollment.system.enrollment.server.domain.repository.FacultyRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.FacultyJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.FacultyJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.FacultyJpaMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alonso
 */
@Adapter
public class FacultyRepositoryAdapter implements FacultyRepository {
  
  private final FacultyJpaRepository facultyJpaRepository;
  private final FacultyJpaMapper facultyJpaMapper;
  
  public FacultyRepositoryAdapter(FacultyJpaRepository facultyJpaRepository,
    FacultyJpaMapper facultyJpaMapper) {
    this.facultyJpaRepository = facultyJpaRepository;
    this.facultyJpaMapper = facultyJpaMapper;
  }
  
  @Override
  public List<Faculty> findAll() {
    return facultyJpaRepository.findAll().stream().map(facultyJpaMapper::toDomainFaculty).toList();
  }
  
  @Override
  public List<Faculty> findAllByIdIn(Set<Integer> ids) {
    return facultyJpaRepository.findAllByFacultyIdIn(ids).stream()
      .map(facultyJpaMapper::toDomainFaculty).toList();
  }
  
  @Override
  public Faculty save(Faculty faculty) {
    FacultyJpaEntity facultyJpaEntity = facultyJpaMapper.toJpaEntity(faculty);
    facultyJpaEntity = facultyJpaRepository.save(facultyJpaEntity);
    return facultyJpaMapper.toDomainFaculty(facultyJpaEntity);
  }
  
  @Override
  public Optional<Faculty> findById(FacultyID facultyID) {
    return facultyJpaRepository.findById(facultyID.getValue())
      .map(facultyJpaMapper::toDomainFaculty);
  }
  
  @Override
  public boolean existsByNameIgnoreCase(String name) {
    return facultyJpaRepository.existsByNameIgnoreCase(name);
  }
  
  @Override
  public boolean existsByNameIgnoreCaseAndFacultyIdNot(String name, FacultyID facultyId) {
    return facultyJpaRepository.existsByNameIgnoreCaseAndFacultyIdNot(name, facultyId.getValue());
  }
  
}
