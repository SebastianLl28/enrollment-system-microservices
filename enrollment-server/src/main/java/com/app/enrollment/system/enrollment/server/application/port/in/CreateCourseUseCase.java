package com.app.enrollment.system.enrollment.server.application.port.in;


import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;

/**
 * @author Alonso
 */
public interface CreateCourseUseCase {

  CourseResponse createCourse(CreateCourseCommand createCourseCommand);

}
