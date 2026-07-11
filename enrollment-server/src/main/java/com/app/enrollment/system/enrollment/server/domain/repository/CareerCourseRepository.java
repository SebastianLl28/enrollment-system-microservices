package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.CareerCourse;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import java.util.List;

/**
 * @author Alonso
 */
public interface CareerCourseRepository {

  List<CareerCourse> findByCourseId(CourseID courseID);

  List<CareerCourse> findByCareerId(CareerID careerID);

  void replaceForCourse(CourseID courseID, List<CareerCourse> careerCourses);

}
