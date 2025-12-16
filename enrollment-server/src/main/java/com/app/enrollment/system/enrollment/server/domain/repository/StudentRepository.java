package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface StudentRepository {
  
  List<Student> findAllByCourseId(Integer courseId);
  
  Student save(Student student);
  
  List<Student> findAll();
  
  Optional<Student> findById(StudentID studentID);
  
  Student mustGet(StudentID studentID);
  
}
