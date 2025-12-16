package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.FacultyJpaEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface FacultyJpaRepository extends JpaRepository<FacultyJpaEntity, Integer> {

  List<FacultyJpaEntity> findAllByFacultyIdIn(Collection<Integer> facultyIds);

  boolean existsByNameIgnoreCase(String name);

  boolean existsByNameIgnoreCaseAndFacultyIdNot(String name, Integer facultyId);
}
