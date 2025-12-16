package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;

/**
 * @author Alonso
 */
public interface GetEnrollmentByIdUseCase {
  
  EnrollmentResponse getEnrollmentById(Integer enrollmentId, UserID userId);
  
}
