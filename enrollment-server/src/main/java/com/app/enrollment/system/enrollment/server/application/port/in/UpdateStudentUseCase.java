package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;

/**
 * @author Alonso
 */
public interface UpdateStudentUseCase {
  StudentResponse updateStudent(UpdateStudentCommand command, Integer studentId);
}
