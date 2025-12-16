package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;

/**
 * @author Alonso
 */
public interface CreateStudentUseCase {

  StudentResponse createStudent(CreateStudentCommand createStudentCommand);

}
