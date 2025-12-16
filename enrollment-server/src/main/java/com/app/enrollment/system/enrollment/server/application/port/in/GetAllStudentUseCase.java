package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllStudentUseCase {

  List<StudentResponse> findAll();

}
