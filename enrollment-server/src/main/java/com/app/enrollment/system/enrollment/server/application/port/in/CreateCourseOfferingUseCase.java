package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseOfferingResponse;

/**
 * @author Alonso
 */
public interface CreateCourseOfferingUseCase {

  CourseOfferingResponse createCourseOffering(CreateCourseOfferingCommand command);

}
