package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface CourseRepository {
  
  Optional<Course> findById(CourseID courseID);
  Course save(Course course);
  List<Course> findAll();
  List<Course> findByCareerIdWithActiveCourses(Integer careerId);
  
}
