package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.CareerSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class EnrollmentMapper {

  private final StudentMapper studentMapper;
  private final CareerOfferingMapper careerOfferingMapper;

  public EnrollmentMapper(StudentMapper studentMapper,
    CareerOfferingMapper careerOfferingMapper) {
    this.studentMapper = studentMapper;
    this.careerOfferingMapper = careerOfferingMapper;
  }

  public EnrollmentResponse toEnrollmentResponse(Enrollment enrollment, Student student,
    CareerOffering careerOffering, CareerSummaryResponse careerSummaryResponse,
    TermResponse termResponse) {
    return new EnrollmentResponse(
      enrollment.getID().getValue(),
      studentMapper.toStudentSummaryResponse(student),
      careerOfferingMapper.toCareerOfferingResponse(careerOffering, careerSummaryResponse,
        termResponse),
      enrollment.getEnrollmentDate(),
      enrollment.getUnenrollmentDate(),
      enrollment.getStatus()
    );
  }



}
