package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllCourseUseCase {
  List<CourseResponse> findAll();
}

