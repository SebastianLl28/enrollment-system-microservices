package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Classroom;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface ClassroomRepository {

  Classroom save(Classroom classroom);

  Optional<Classroom> findById(ClassroomID classroomID);

  List<Classroom> findAll();

}
