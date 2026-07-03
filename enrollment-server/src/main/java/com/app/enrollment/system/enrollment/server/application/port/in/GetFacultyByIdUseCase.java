package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;

/**
 * @author Alonso
 */
public interface GetFacultyByIdUseCase {
  FacultyResponse findById(Integer id);
}
