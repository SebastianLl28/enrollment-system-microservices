package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.exception.IllegalCourseIDException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCarreerException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLevelException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SemesterLevel;

/**
 * Relación curso-carrera de la malla curricular: un curso puede dictarse en
 * varias carreras y el ciclo (semesterLevel) puede variar por carrera.
 *
 * @author Alonso
 */
public class CareerCourse {

  private final CareerID careerId;
  private final CourseID courseId;
  private final SemesterLevel semesterLevel;

  private CareerCourse(CareerID careerId, CourseID courseId, SemesterLevel semesterLevel) {
    this.careerId = careerId;
    this.courseId = courseId;
    this.semesterLevel = semesterLevel;
  }

  public static CareerCourse create(CareerID careerId, CourseID courseId, SemesterLevel semesterLevel) {
    if (careerId == null) throw new InvalidCarreerException("careerId es obligatorio.");
    if (courseId == null) throw new IllegalCourseIDException("courseId es obligatorio.");
    if (semesterLevel == null) throw new InvalidSemesterLevelException("semesterLevel es obligatorio.");
    return new CareerCourse(careerId, courseId, semesterLevel);
  }

  public CareerID getCareerId() {
    return careerId;
  }

  public CourseID getCourseId() {
    return courseId;
  }

  public SemesterLevel getSemesterLevel() {
    return semesterLevel;
  }
}
