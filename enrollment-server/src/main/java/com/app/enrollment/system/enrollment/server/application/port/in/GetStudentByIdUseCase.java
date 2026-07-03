package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;

/**
 * @author Alonso
 */
public interface GetStudentByIdUseCase {
  StudentResponse findById(Integer studentId);
}
