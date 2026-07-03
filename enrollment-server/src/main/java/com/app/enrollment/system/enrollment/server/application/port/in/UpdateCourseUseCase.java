package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;

/**
 * @author Alonso
 */
public interface UpdateCourseUseCase {
  CourseResponse updateCourse(UpdateCourseCommand command, Integer courseId);
}
