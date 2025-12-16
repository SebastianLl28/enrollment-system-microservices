package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.query.EnrollmentQuery;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllEnrollmentCourseUseCase {
  
  List<EnrollmentResponse> getAllEnrollmentCourses(EnrollmentQuery query);
  
}
