package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alonso
 */
public interface FacultyRepository {
  
  List<Faculty> findAll();
  
  List<Faculty> findAllByIdIn(Set<Integer> ids);
  
  Faculty save(Faculty faculty);
  
  Optional<Faculty> findById(FacultyID id);
  
  boolean existsByNameIgnoreCase(String name);
  
  boolean existsByNameIgnoreCaseAndFacultyIdNot(String name, FacultyID facultyId);

}
