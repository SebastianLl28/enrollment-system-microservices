package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCourseOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseOfferingResponse;

/**
 * @author Alonso
 */
public interface UpdateCourseOfferingUseCase {
  CourseOfferingResponse updateCourseOffering(UpdateCourseOfferingCommand command,
    Integer courseOfferingId);
}
