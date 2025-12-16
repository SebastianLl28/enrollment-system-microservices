package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;

/**
 * @author Alonso
 */
public interface CreateEnrollmentUseCase {
  
  EnrollmentResponse createEnrollment(CreateEnrollmentCommand command, Integer userId);

}
