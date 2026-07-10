package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.query.EnrollmentQuery;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.PageResponse;

/**
 * @author Alonso
 */
public interface GetAllEnrollmentCourseUseCase {

  PageResponse<EnrollmentResponse> getAllEnrollmentCourses(EnrollmentQuery query);
  
}
