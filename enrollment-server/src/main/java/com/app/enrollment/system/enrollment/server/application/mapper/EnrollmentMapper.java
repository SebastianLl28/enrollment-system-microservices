package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class EnrollmentMapper {
  
  private final StudentMapper studentMapper;
  private final CourseOfferingMapper courseOfferingMapper;
  
  public EnrollmentMapper(StudentMapper studentMapper,
    CourseOfferingMapper courseOfferingMapper) {
    this.studentMapper = studentMapper;
    this.courseOfferingMapper = courseOfferingMapper;
  }
  
  public EnrollmentResponse toEnrollmentResponse(Enrollment enrollment, Student student, CourseOffering courseOffering, CourseSummaryResponse courseSummaryResponse, TermResponse termResponse) {
    return new EnrollmentResponse(
      enrollment.getID().getValue(),
      studentMapper.toStudentSummaryResponse(student),
      courseOfferingMapper.toCourseOfferingResponse(courseOffering, courseSummaryResponse, termResponse),
      enrollment.getEnrollmentDate(),
      enrollment.getUnenrollmentDate(),
      enrollment.getStatus()
    );
  }
  
  
  
}
