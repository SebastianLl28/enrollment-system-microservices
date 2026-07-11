package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllClassroomUseCase {

  List<ClassroomResponse> getAllClassrooms();

}
