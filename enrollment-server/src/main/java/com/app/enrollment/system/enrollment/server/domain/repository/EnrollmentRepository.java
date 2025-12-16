package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface EnrollmentRepository {
  
  Enrollment save(Enrollment enrollment);
  
  Optional<Enrollment> findByStudentIDAndCourseOfferingIDAndStatus(StudentID studentID, CourseOfferingID courseOfferingID, EnrollmentStatus enrollmentStatus);
  
  List<Enrollment> findAllByStudentIDAndTermIDAndCourseID(StudentID studentID, TermID termID, CourseID courseID);
  
  Optional<Enrollment> findByEnrollmentID(EnrollmentID enrollmentID);
  
}
