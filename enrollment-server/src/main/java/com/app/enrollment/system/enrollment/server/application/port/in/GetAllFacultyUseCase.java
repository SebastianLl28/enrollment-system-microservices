package com.app.enrollment.system.enrollment.server.application.port.in;


import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllFacultyUseCase {
  List<FacultyResponse> findAll();
}
