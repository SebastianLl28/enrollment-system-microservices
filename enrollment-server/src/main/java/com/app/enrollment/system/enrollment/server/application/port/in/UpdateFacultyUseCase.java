package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateFacultyCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;

/**
 * @author Alonso
 */
public interface UpdateFacultyUseCase {
  
  FacultyResponse updateFaculty(UpdateFacultyCommand updateFacultyCommand, Integer facultyId);
  
}
