package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateFacultyCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;

/**
 * @author Alonso
 */
public interface CreateFacultyUseCase {

  FacultyResponse createFaculty(CreateFacultyCommand createFacultyCommand);

}
